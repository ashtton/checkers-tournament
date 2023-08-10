package me.gleeming.backend.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Message {

    private String author;
    private String message;
    private long date;
    private boolean deleted;

}
