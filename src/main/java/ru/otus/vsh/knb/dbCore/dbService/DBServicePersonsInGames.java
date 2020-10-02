package ru.otus.vsh.knb.dbCore.dbService;

import ru.otus.vsh.knb.dbCore.dbService.api.DBService;
import ru.otus.vsh.knb.dbCore.model.Game;
import ru.otus.vsh.knb.dbCore.model.Person;
import ru.otus.vsh.knb.dbCore.model.PersonsInGames;
import ru.otus.vsh.knb.dbCore.model.Roles;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public interface DBServicePersonsInGames extends DBService<PersonsInGames> {
    List<PersonsInGames> personsByGame(@Nonnull Game game);

    void joinGame(@Nonnull Game game, @Nonnull Person person, @Nonnull Roles roles);

    Optional<PersonsInGames> personByGameAndRolePlayer1(@Nonnull Game game);

    Optional<PersonsInGames> personByGameAndRolePlayer2(@Nonnull Game game);

    List<PersonsInGames> personByGameAndRoleObserver(@Nonnull Game game);

    Optional<PersonsInGames> personByGameAndPlayer(@Nonnull Game game, @Nonnull Person person);

}
