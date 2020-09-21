package ru.otus.vsh.knb.domain;

import lombok.experimental.UtilityClass;
import lombok.val;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class FigureUtils {

    public static final int maxGame = 15;
    public static final int minGame = 3;

    public boolean isNValid(int n) {
        return minGame <= n && n <= maxGame && n % 2 == 1;
    }

    public List<Figure> getNFigures(int n) {
        if (!isNValid(n)) throw new GameException(String.format("Cannot create game with n = %d", n));
        return Arrays.stream(Figure.values())
                .filter(figure -> figure.order < n)
                .collect(Collectors.toList());
    }

    @Nonnull
    public Figure nextFigure(@Nonnull List<Figure> figures, @Nonnull Figure figure) {
        return nextFigure(figures, figure, 1);
    }

    @Nonnull
    public Figure nextFigure(@Nonnull List<Figure> figures, @Nonnull Figure figure, int step) {
        int nextIndex = figures.indexOf(figure) - step;
        if (nextIndex < 0) {
            nextIndex += figures.size();
        }
        return figures.get(nextIndex);
    }

    @Nonnull
    private List<Figure> getNWinMapFigure(int n, @Nonnull Figure figure) {
        if (isNValid(n)) throw new GameException(String.format("Cannot create game with n = %d", n));
        return getNFigures(n)
                .stream()
                .filter(f -> {
                    val half = (n - 1) / 2;
                    if ((figure.order + half) < n) {
                        return (figure.order + 1) <= f.order && f.order <= (figure.order + half);
                    } else {
                        val remainder = figure.order + half - n;
                        return ((figure.order + 1) <= f.order && f.order <= (n - 1)) ||
                                (0 <= f.order && f.order <= remainder);
                    }
                })
                .collect(Collectors.toList());
    }

    @Nonnull
    public static Map<Figure, List<Figure>> getNWinMap(int n) {
        if (isNValid(n)) throw new GameException(String.format("Cannot create game with n = %d", n));
        val result = new HashMap<Figure, List<Figure>>(n);
        val figures = getNFigures(n);
        for (val figure : figures) {
            result.put(figure, getNWinMapFigure(n, figure));
        }
        return result;
    }

}
