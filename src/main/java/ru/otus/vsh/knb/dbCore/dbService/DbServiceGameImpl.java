package ru.otus.vsh.knb.dbCore.dbService;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Repository;
import ru.otus.vsh.knb.dbCore.dao.GameDao;
import ru.otus.vsh.knb.dbCore.dbService.api.AbstractDbServiceImpl;
import ru.otus.vsh.knb.dbCore.model.Game;

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

}
