package ru.otus.vsh.knb.webCore.gamePage.data;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import ru.otus.vsh.knb.domain.Figure;
import ru.otus.vsh.knb.domain.GameException;

import java.util.Arrays;

@Data
public class UITurnEnd {
    String sessionId;
    @Getter(AccessLevel.NONE)
    long figureId;

    public Figure getFigure() {
        return Arrays.stream(Figure.values())
                .filter(f -> f.getOrder() == figureId)
                .findFirst()
                .orElseThrow(() -> new GameException(String.format("Incorrect figure id = %d", figureId)));
    }
}
