package ru.otus.vsh.knb.hibernate.dao;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import ru.otus.vsh.knb.dbCore.dao.BetDao;
import ru.otus.vsh.knb.dbCore.model.Bet;
import ru.otus.vsh.knb.dbCore.model.PersonsInGames;
import ru.otus.vsh.knb.hibernate.sessionmanager.SessionManagerHibernate;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class BetDaoHibernate extends AbstractDaoHibernate<Bet> implements BetDao {

    public BetDaoHibernate(SessionManagerHibernate sessionManager) {
        super(sessionManager, Bet.class);
    }

    @Override
    public List<Bet> findByPersonInGame(@Nonnull PersonsInGames person) {
        try {
            val entityManager = sessionManager.getEntityManager();
            return entityManager
                    .createNamedQuery(Bet.GET_BETS_BY_PIG, Bet.class)
                    .setParameter("personId", person.getId())
                    .getResultList();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    @Override
    public Optional<Bet> findCommonBet(@Nonnull PersonsInGames person1, @Nonnull PersonsInGames person2) {
        try {
            val entityManager = sessionManager.getEntityManager();
            val bets = entityManager
                    .createNamedQuery(Bet.GET_BETS_BY_PIGS, Bet.class)
                    .setParameter("person1Id", person1.getId())
                    .setParameter("person2Id", person2.getId())
                    .getResultList();
            if (bets.isEmpty()) return Optional.empty();
            else return Optional.of(bets.get(0));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Bet> findOpeningBet(@Nonnull PersonsInGames person) {
        try {
            val entityManager = sessionManager.getEntityManager();
            val bets = entityManager
                    .createNamedQuery(Bet.GET_BET_WITHOUT_SND_PIG, Bet.class)
                    .setParameter("personId", person.getId())
                    .getResultList();
            if (bets.isEmpty()) return Optional.empty();
            else return Optional.of(bets.get(0));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }
}
