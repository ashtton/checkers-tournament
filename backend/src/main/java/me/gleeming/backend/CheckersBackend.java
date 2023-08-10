package me.gleeming.backend;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import io.javalin.Javalin;
import me.gleeming.backend.match.Match;
import me.gleeming.backend.player.Message;
import me.gleeming.backend.player.Player;

import java.util.*;

public class CheckersBackend {


    private static final List<Match> matches = new ArrayList<>();
    private static final Random random = new Random();

    public static void main(String[] args) throws InterruptedException {
        Javalin javalin = Javalin.create();

        javalin.after(ctx -> ctx.header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "*"));

        javalin.get("/room/{name}", ctx -> {
            Optional<Match> matchOptional = matches.stream()
                    .filter(match -> match.getMatchId().equals(ctx.pathParam("name")))
                    .findFirst();

            if (matchOptional.isEmpty()) {
                ctx.status(404).result("Not found");
                return;
            }

            ctx.result(matchOptional.get().getMatchId());
        });

        javalin.post("/room/", ctx -> {
            Match match = new Match();
            match.setMatchId(String.valueOf((1 + random.nextInt(2)) * 10000 + random.nextInt(10000)));
            matches.add(match);
            ctx.result(match.getMatchId());
        });

        javalin.start(7070);

        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(3465);

        SocketIOServer server = new SocketIOServer(config);

        server.addEventListener("disband", String.class, ((socketIOClient, s, ackRequest) -> {
            matches.stream().filter(match -> match.getMatchOwner() == socketIOClient.getSessionId())
                    .forEach(match -> match.getPlayers().forEach(player -> server.getClient(player.getClientId()).sendEvent("lobbyDeleted")));
        }));

        server.addEventListener("sendMessage", String.class, (((socketIOClient, s, ackRequest) -> {
            matches.stream().filter(match -> match.getPlayers().stream().anyMatch(player -> player.getClientId() == socketIOClient.getSessionId()))
                    .forEach(match -> match.getPlayers().forEach(player -> server.getClient(player.getClientId())
                            .sendEvent("newMessage", new Message(match.getPlayers().stream().filter(p -> p.getClientId() == socketIOClient.getSessionId())
                                    .findFirst().get().getUsername(), s, System.currentTimeMillis(), false))));
        })));

        server.addEventListener("join", String.class, (socketIOClient, s, ackRequest) -> {
            String[] array = s.split(",");

            String matchId = array[0];
            String nickname = array[1];

            Optional<Match> matchOptional = matches.stream()
                    .filter(match -> match.getMatchId().equals(matchId))
                    .findFirst();

            if (matchOptional.isEmpty()) {
                return;
            }

            Player player = new Player();
            player.setClientId(socketIOClient.getSessionId());
            player.setUsername(nickname);

            if (matchOptional.get().getPlayers().size() == 0) {
                player.setOwner(true);
                matchOptional.get().setMatchOwner(player.getClientId());
            }

            matchOptional.get().getPlayers().add(player);

            System.out.println(nickname + " has joined " + matchId);
        });

        server.addConnectListener(socketIOClient -> {
            System.out.println(socketIOClient.getSessionId().toString() + " connected");
        });

        server.addDisconnectListener(socketIOClient -> {
            System.out.println(socketIOClient.getSessionId().toString() + " disconnected");
            matches.forEach(match -> match.getPlayers().removeIf(player -> player.getClientId() == socketIOClient.getSessionId()));
        });

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                matches.forEach(match -> match.getPlayers().forEach(player -> {
                    server.getClient(player.getClientId()).sendEvent("updateGame", match);
                }));
            }
        }, 0, 500);

        server.start();
        Thread.sleep(Integer.MAX_VALUE);

        server.stop();
    }

}
