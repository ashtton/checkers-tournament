package me.gleeming.backend.match;

import lombok.Getter;
import lombok.Setter;
import me.gleeming.backend.player.Player;

import java.util.*;

@Getter @Setter
public class Match {

    private String matchId;
    private UUID matchOwner;
    private MatchState matchState = MatchState.LOBBY;

    private final List<Player> players = new ArrayList<>();

    public enum MatchState {
        LOBBY, PLAYING, ENDED
    }

}
