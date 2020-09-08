package ru.otus.vsh.knb.dbCore.dbService;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Repository;
import ru.otus.vsh.knb.dbCore.dao.BetDao;
import ru.otus.vsh.knb.dbCore.dbService.api.AbstractDbServiceImpl;
import ru.otus.vsh.knb.dbCore.model.Bet;
import ru.otus.vsh.knb.dbCore.model.PersonsInGames;

import java.util.HashMap;
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

    @Override
    public List<Bet> findCommonBets(PersonsInGames person1, PersonsInGames person2) {
        String p1 = "p1";
        String p2 = "p2";
        val params = new HashMap<String, PersonsInGames>(2);
        params.put(p1, person1);
        params.put(p2, person2);
        return executeInSession((sm, pigs) -> {
            val foundBets = betDao.findCommonBets(pigs.get(p1), pigs.get(p2));
            log.info("found bets: {}", foundBets);
            sm.commitSession();

            return foundBets;
        }, params);
    }

}
