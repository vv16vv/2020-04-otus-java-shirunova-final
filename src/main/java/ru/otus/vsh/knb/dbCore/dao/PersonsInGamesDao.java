package ru.otus.vsh.knb.dbCore.dao;

import ru.otus.vsh.knb.dbCore.model.Game;
import ru.otus.vsh.knb.dbCore.model.Person;
import ru.otus.vsh.knb.dbCore.model.PersonsInGames;
import ru.otus.vsh.knb.dbCore.model.Roles;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public interface PersonsInGamesDao extends Dao<PersonsInGames> {
    List<PersonsInGames> personsByGame(@Nonnull Game game);

    List<PersonsInGames> personByGameAndRole(@Nonnull Game game, @Nonnull Roles role);

    Optional<PersonsInGames> personByGameAndPlayer(@Nonnull Game game, @Nonnull Person person);

    void joinGame(@Nonnull Game game, @Nonnull Person person, @Nonnull Roles role);
}
