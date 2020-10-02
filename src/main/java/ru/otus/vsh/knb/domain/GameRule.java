package ru.otus.vsh.knb.domain;

import lombok.val;

import java.util.List;
import java.util.Map;
import java.util.Random;

public interface GameRule {
    int getSize();

    List<Figure> getRange();

    Map<Figure, List<Figure>> getWinMap();

    default Figure randomValue() {
        val randomIndex = (new Random()).nextInt(getSize());
        return getRange().get(randomIndex);
    }

    default boolean isWin(Figure first, Figure second) {
        return getWinMap().get(first).contains(second);
    }

    default boolean isLost(Figure first, Figure second) {
        return isWin(second, first);
    }

    default boolean isDraw(Figure first, Figure second) {
        return first == second;
    }
}
