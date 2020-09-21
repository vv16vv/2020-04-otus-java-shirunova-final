package ru.otus.vsh.knb.domain;

import java.util.List;
import java.util.Map;

public class GameRuleN implements GameRule {
    private final int size;
    private final List<Figure> range;
    private final Map<Figure, List<Figure>> winMap;

    public GameRuleN(int size) {
        this.size = size;
        range = FigureUtils.getNFigures(size);
        winMap = FigureUtils.getNWinMap(size);
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public List<Figure> getRange() {
        return range;
    }

    @Override
    public Map<Figure, List<Figure>> getWinMap() {
        return winMap;
    }
}
