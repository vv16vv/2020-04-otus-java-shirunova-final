package ru.otus.vsh.knb.domain;

import lombok.Value;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
public class GameRules {
    private final Map<Integer, GameRule> rules = new ConcurrentHashMap<>();
    private final Object lock = new Object();

    public Optional<GameRule> get(int size) {
        if (FigureUtils.isNValid(size)) {
            if (!rules.containsKey(size)) {
                synchronized (lock) {
                    rules.put(size, new GameRuleImpl(size));
                }
            }
            return Optional.of(rules.get(size));
        }
        return Optional.empty();
    }

    @Value
    private static class GameRuleImpl implements GameRule {
        int size;
        List<Figure> range;
        Map<Figure, List<Figure>> winMap;

        private GameRuleImpl(int size) {
            this.size = size;
            range = FigureUtils.getNFigures(size);
            winMap = FigureUtils.getNWinMap(size);
        }
    }
}
