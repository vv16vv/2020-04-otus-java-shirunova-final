package ru.otus.vsh.knb.webCore.lobbyPage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Accessors(fluent = true)
public enum AvailGameStyles {
    AVAILABLE_TO_PLAY("availableToPlay"),
    AVAILABLE_TO_OBSERVE("availableToObserve"),
    NOT_AVAILABLE("notAvailable");
    @Getter
    private final String title;
}
