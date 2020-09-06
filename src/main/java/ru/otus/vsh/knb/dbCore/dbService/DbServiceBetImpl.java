package ru.otus.vsh.knb.dbCore.dbService;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Repository;
import ru.otus.vsh.knb.dbCore.dao.BetDao;
import ru.otus.vsh.knb.dbCore.dbService.api.AbstractDbServiceImpl;
import ru.otus.vsh.knb.dbCore.model.Bet;
import ru.otus.vsh.knb.dbCore.model.PersonsInGames;

import java.util.List;

@Slf4j
@Repository
public class DbServiceBetImpl extends AbstractDbServiceImpl<Bet> implements DBServiceBet {
    private final BetDao betDao;

    public DbServiceBetImpl(BetDao betDao) {
        super(betDao);
        this.betDao = betDao;
    }

    @Override
    public List<Bet> findByPersonInGame(PersonsInGames person) {
        return executeInSession((sm, pig) -> {
            val foundBets = betDao.findByPersonInGame(pig);
            log.info("found bets: {}", foundBets);
            sm.commitSession();

            return foundBets;
        }, person);
    }
}
