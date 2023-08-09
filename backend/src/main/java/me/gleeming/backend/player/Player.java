package me.gleeming.backend.player;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
public class Player {

    private UUID clientId;
    private String username;

    private boolean owner;

}
