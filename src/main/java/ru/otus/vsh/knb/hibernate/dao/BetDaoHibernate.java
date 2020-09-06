package ru.otus.vsh.knb.hibernate.dao;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import ru.otus.vsh.knb.dbCore.dao.BetDao;
import ru.otus.vsh.knb.dbCore.model.Bet;
import ru.otus.vsh.knb.dbCore.model.PersonsInGames;
import ru.otus.vsh.knb.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class BetDaoHibernate extends AbstractDaoHibernate<Bet> implements BetDao {

    public BetDaoHibernate(SessionManagerHibernate sessionManager) {
        super(sessionManager, Bet.class);
    }

    @Override
    public List<Bet> findByPersonInGame(PersonsInGames person) {
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
}
