package me.gleeming.backend.match;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter @Setter
public class Match {

    private String matchId;

    private final Map<UUID, String> clients = new HashMap<>();

}
