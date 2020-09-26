package ru.otus.vsh.knb.dbCore.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Arrays;

@AllArgsConstructor
@Accessors(fluent = true)
public enum Roles {
    Player1(1, "Игрок 1"),
    Player2(2, "Игрок 2"),
    Observer(3, "Наблюдатель"),
    NotParticipate(4, "Не участвует");

    @Getter
    private final int id;
    @Getter
    private final String title;

    public static Roles fromId(int id) {
        return Arrays.stream(values())
                .filter(role -> role.id() == id)
                .findFirst()
                .orElse(Observer);
    }
}
