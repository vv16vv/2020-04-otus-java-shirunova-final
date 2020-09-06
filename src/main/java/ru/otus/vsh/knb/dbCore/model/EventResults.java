package ru.otus.vsh.knb.dbCore.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Arrays;

@AllArgsConstructor
@Accessors(fluent = true)
public enum EventResults {
    Player1Won(1),
    Player2Won(2),
    Draw(3),
    Unknown(4);

    @Getter
    private final int id;

    public static EventResults fromId(int id) {
        return Arrays.stream(values())
                .filter(result -> result.id() == id)
                .findFirst()
                .orElse(Unknown);
    }
}
