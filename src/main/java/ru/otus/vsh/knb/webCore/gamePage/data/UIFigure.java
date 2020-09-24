package ru.otus.vsh.knb.webCore.gamePage.data;

import lombok.Value;
import ru.otus.vsh.knb.domain.Figure;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

@Value
public class UIFigure {
    int icon;
    String itemName;

    @Nonnull
    static public UIFigure from(@Nonnull Figure figure) {
        return new UIFigure(figure.getOrder(), figure.getTitle());
    }

    @Nonnull
    static public List<UIFigure> from(@Nonnull List<Figure> figures) {
        return figures.stream()
                .map(UIFigure::from)
                .collect(Collectors.toList());
    }
}
