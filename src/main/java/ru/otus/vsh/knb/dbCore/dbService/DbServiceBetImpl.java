package ru.otus.vsh.knb.dbCore.dbService;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Repository;
import ru.otus.vsh.knb.dbCore.dao.BetDao;
import ru.otus.vsh.knb.dbCore.dbService.api.AbstractDbServiceImpl;
import ru.otus.vsh.knb.dbCore.model.Bet;
import ru.otus.vsh.knb.dbCore.model.EventResults;
import ru.otus.vsh.knb.dbCore.model.PersonsInGames;
import ru.otus.vsh.knb.hibernate.dao.PersonsInGamesDaoHibernate;
import ru.otus.vsh.knb.hibernate.sessionmanager.SessionManagerHibernate;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class DbServiceBetImpl extends AbstractDbServiceImpl<Bet> implements DBServiceBet {
    private final BetDao betDao;

    public DbServiceBetImpl(BetDao betDao) {
        super(betDao);
        this.betDao = betDao;
    }

    @Override
    public List<Bet> findByPersonInGame(@Nonnull PersonsInGames person) {
        return executeInSession((sm, pig) -> {
            val foundBets = betDao.findByPersonInGame(pig);
            log.info("found bets: {}", foundBets);
            sm.commitSession();

            return foundBets;
        }, person);
    }

    @Override
    public Optional<Bet> findCommonBet(@Nonnull PersonsInGames person1, @Nonnull PersonsInGames person2) {
        String p1 = "p1";
        String p2 = "p2";
        val params = new HashMap<String, PersonsInGames>(2);
        params.put(p1, person1);
        params.put(p2, person2);
        return executeInSession((sm, pigs) -> {
            val foundBet = betDao.findCommonBet(pigs.get(p1), pigs.get(p2));
            log.info("found bet between {} and {}: {}",
                    pigs.get(p1).getPerson().getLogin(),
                    pigs.get(p2).getPerson().getLogin(),
                    foundBet);
            sm.commitSession();

            return foundBet;
        }, params);
    }

    @Override
    public Optional<Bet> findGameBet(@Nonnull PersonsInGames person1, @Nonnull Optional<PersonsInGames> person2) {
        return executeInSession((sm, notUsed) -> {
            Optional<Bet> foundBet;
            if (person2.isEmpty()) {
                foundBet = betDao.findOpeningBet(person1);
            } else {
                foundBet = betDao.findCommonBet(person1, person2.get());
            }

            log.info("found bet: {}", foundBet);
            sm.commitSession();

            return foundBet;
        }, null);
    }

    @Override
    public boolean makeBet(@Nonnull PersonsInGames person1, @Nonnull PersonsInGames person2, long wager, @Nonnull EventResults expectedResult) {
        if (!person1.getPerson().canAfford(wager) || !person2.getPerson().canAfford(wager)) return false;
        return executeInSession((sm, notUsed) -> {
            val bet = Bet.builder()
                    .person1(person1)
                    .person2(person2)
                    .expectedResult(expectedResult.id())
                    .wager(wager)
                    .get();
            betDao.insert(bet);
            val pigDao = new PersonsInGamesDaoHibernate((SessionManagerHibernate) sm);
            person1.getPerson().getAccount().decrease(wager);
            person2.getPerson().getAccount().decrease(wager);
            pigDao.update(person1);
            pigDao.update(person2);

            sm.commitSession();

            return true;
        }, null);
    }
}
