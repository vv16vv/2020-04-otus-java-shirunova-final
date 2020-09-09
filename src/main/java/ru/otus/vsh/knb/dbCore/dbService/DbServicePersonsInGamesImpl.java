package ru.otus.vsh.knb.dbCore.dbService;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Repository;
import ru.otus.vsh.knb.dbCore.dao.PersonsInGamesDao;
import ru.otus.vsh.knb.dbCore.dbService.api.AbstractDbServiceImpl;
import ru.otus.vsh.knb.dbCore.model.Game;
import ru.otus.vsh.knb.dbCore.model.Person;
import ru.otus.vsh.knb.dbCore.model.PersonsInGames;
import ru.otus.vsh.knb.dbCore.model.Roles;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class DbServicePersonsInGamesImpl extends AbstractDbServiceImpl<PersonsInGames> implements DBServicePersonsInGames {
    private final PersonsInGamesDao personsInGamesDao;

    public DbServicePersonsInGamesImpl(PersonsInGamesDao personsInGamesDao) {
        super(personsInGamesDao);
        this.personsInGamesDao = personsInGamesDao;
    }

    @Override
    public List<PersonsInGames> personsByGame(@Nonnull Game game) {
        return executeInSession((sm, byGame) -> {
            var persons = personsInGamesDao.personsByGame(byGame);
            sm.commitSession();
            log.info("found persons participating in game = {}: {}", game, persons);
            return persons;
        }, game);
    }

    @Override
    public void joinGame(@Nonnull Game game, @Nonnull Person person, @Nonnull Roles roles) {
        executeInSession((sm, notUsed) -> {
            personsInGamesDao.joinGame(game, person, roles);
            sm.commitSession();
            return null;
        }, null);
    }

    @Override
    public Optional<PersonsInGames> personByGameAndRolePlayer1(@Nonnull Game game) {
        val pigs = personByGameAndRole(game, Roles.Player1);
        if (pigs.isEmpty()) return Optional.empty();
        else return Optional.of(pigs.get(0));
    }

    @Override
    public Optional<PersonsInGames> personByGameAndRolePlayer2(@Nonnull Game game) {
        val pigs = personByGameAndRole(game, Roles.Player2);
        if (pigs.isEmpty()) return Optional.empty();
        else return Optional.of(pigs.get(0));
    }

    @Override
    public List<PersonsInGames> personByGameAndRoleObserver(@Nonnull Game game) {
        return personByGameAndRole(game, Roles.Observer);
    }

    private List<PersonsInGames> personByGameAndRole(@Nonnull Game game, @Nonnull Roles role) {
        return executeInSession((sm, notUsed) -> {
            val pig = personsInGamesDao.personByGameAndRole(game, role);
            sm.commitSession();
            return pig;
        }, null);
    }

    @Override
    public Optional<PersonsInGames> personByGameAndPlayer(@Nonnull Game game, @Nonnull Person person) {
        return executeInSession((sm, notUsed) -> {
            val pig = personsInGamesDao.personByGameAndPlayer(game, person);
            sm.commitSession();
            return pig;
        }, null);
    }
}
