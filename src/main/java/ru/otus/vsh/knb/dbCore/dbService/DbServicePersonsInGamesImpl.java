package ru.otus.vsh.knb.dbCore.dbService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.otus.vsh.knb.dbCore.dao.PersonsInGamesDao;
import ru.otus.vsh.knb.dbCore.dbService.api.AbstractDbServiceImpl;
import ru.otus.vsh.knb.dbCore.model.Game;
import ru.otus.vsh.knb.dbCore.model.Person;
import ru.otus.vsh.knb.dbCore.model.PersonsInGames;

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
    public List<PersonsInGames> personsByGame(Game game){
        return executeInSession((sm, byGame) -> {
            var persons = personsInGamesDao.personsByGame(byGame);
            sm.commitSession();
            log.info("found persons participating in game = {}: {}", game, persons);
            return persons;
        }, game);
    }

}
