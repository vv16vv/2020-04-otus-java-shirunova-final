package ru.otus.vsh.knb.dbCore.dao;

import ru.otus.vsh.knb.dbCore.model.Game;
import ru.otus.vsh.knb.dbCore.model.GameSettings;
import ru.otus.vsh.knb.dbCore.model.Person;

import javax.annotation.Nonnull;
import java.util.List;

public interface GameDao extends Dao<Game> {
    List<Game> activeGames();

    Game createNewGame(@Nonnull Person person, @Nonnull GameSettings settings, long wager);
}
