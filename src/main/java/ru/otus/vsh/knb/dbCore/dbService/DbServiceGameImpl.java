package ru.otus.vsh.knb.dbCore.dbService;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Repository;
import ru.otus.vsh.knb.dbCore.dao.GameDao;
import ru.otus.vsh.knb.dbCore.dbService.api.AbstractDbServiceImpl;
import ru.otus.vsh.knb.dbCore.model.Game;
import ru.otus.vsh.knb.dbCore.model.GameSettings;
import ru.otus.vsh.knb.dbCore.model.Person;

import javax.annotation.Nonnull;
import java.util.List;

@Slf4j
@Repository
public class DbServiceGameImpl extends AbstractDbServiceImpl<Game> implements DBServiceGame {
    private final GameDao gameDao;

    public DbServiceGameImpl(GameDao gameDao) {
        super(gameDao);
        this.gameDao = gameDao;
    }

    @Override
    public List<Game> activeGames() {
        return executeInSession((sm, notUsed) -> {
            val games = gameDao.activeGames();
            log.info("found games: {}", games);
            sm.commitSession();
            return games;
        }, null);
    }

    @Override
    public Game createNewGame(@Nonnull Person person) {
        return createNewGame(person, GameSettings.builder().get(), 0L);
    }

    @Override
    public Game createNewGame(@Nonnull Person person, @Nonnull GameSettings settings) {
        return createNewGame(person, settings, 0L);
    }

    @Override
    public Game createNewGame(@Nonnull Person person, long wager) {
        return createNewGame(person, GameSettings.builder().get(), wager);
    }

    @Override
    public Game createNewGame(@Nonnull Person person, @Nonnull GameSettings settings, long wager) {
        return executeInSession((sm, notUsed) -> {
            val game = gameDao.createNewGame(person, settings, wager);
            person.getAccount().decrease(wager);
            log.info("created game: {}", game);
            sm.commitSession();
            return game;
        }, null);
    }
}
