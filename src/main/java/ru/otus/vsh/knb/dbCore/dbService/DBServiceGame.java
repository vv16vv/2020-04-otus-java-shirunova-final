package ru.otus.vsh.knb.dbCore.dbService;

import ru.otus.vsh.knb.dbCore.dbService.api.DBService;
import ru.otus.vsh.knb.dbCore.model.Game;
import ru.otus.vsh.knb.dbCore.model.GameSettings;
import ru.otus.vsh.knb.dbCore.model.Person;

import javax.annotation.Nonnull;
import java.util.List;

public interface DBServiceGame extends DBService<Game> {
    Game createNewGame(@Nonnull Person person);
    Game createNewGame(@Nonnull Person person, @Nonnull GameSettings settings);
    Game createNewGame(@Nonnull Person person, long wager);
    Game createNewGame(@Nonnull Person person, @Nonnull GameSettings settings, long wager);

    List<Game> activeGames();
}
