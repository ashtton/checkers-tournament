package me.gleeming.backend;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import io.javalin.Javalin;
import lombok.Getter;
import lombok.Setter;
import me.gleeming.backend.match.Match;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class CheckersBackend {


    private static final List<Match> matches = new ArrayList<>();
    private static final Random random = new Random();

    public static void main(String[] args) throws InterruptedException {
        Javalin javalin = Javalin.create();

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
            ctx.result(match.getMatchId());
        });

        javalin.post("/room/{name}/join", ctx -> {

        });

        javalin.start(7070);

        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(3465);

        SocketIOServer server = new SocketIOServer(config);

        server.addEventListener("event1", String.class, (socketIOClient, s, ackRequest) -> {
            System.out.println(s);
        });

        server.addConnectListener(socketIOClient -> {
            System.out.println(socketIOClient.getSessionId().toString() + " connected");
        });

        server.addDisconnectListener(socketIOClient -> {
            System.out.println(socketIOClient.getSessionId().toString() + " disconnected");
            matches.stream().filter(match -> match.getClients().containsKey(socketIOClient.getSessionId()))
                    .forEach(match -> match.getClients().remove(socketIOClient.getSessionId()));
        });

        server.start();
        Thread.sleep(Integer.MAX_VALUE);

        server.stop();
    }

}
