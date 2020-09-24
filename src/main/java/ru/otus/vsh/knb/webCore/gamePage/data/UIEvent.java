package ru.otus.vsh.knb.webCore.gamePage.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum UIEvent {
    GREATER(">"),
    LESS("<"),
    EQUAL("=");

    @Getter
    private final String title;

}
