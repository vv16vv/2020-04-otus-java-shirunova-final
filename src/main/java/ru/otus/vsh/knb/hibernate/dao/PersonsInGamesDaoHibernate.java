package ru.otus.vsh.knb.hibernate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.otus.vsh.knb.dbCore.dao.PersonsInGamesDao;
import ru.otus.vsh.knb.dbCore.model.Game;
import ru.otus.vsh.knb.dbCore.model.PersonsInGames;
import ru.otus.vsh.knb.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class PersonsInGamesDaoHibernate extends AbstractDaoHibernate<PersonsInGames> implements PersonsInGamesDao {

    public PersonsInGamesDaoHibernate(SessionManagerHibernate sessionManager) {
        super(sessionManager, PersonsInGames.class);
    }

    @Override
    public List<PersonsInGames> personsByGame(Game game) {
        try {
            var entityManager = sessionManager.getEntityManager();
            return entityManager
                    .createNamedQuery(PersonsInGames.GET_ALL_PERSONS_BY_GAME, PersonsInGames.class)
                    .setParameter("gameId", game.getId())
                    .getResultList();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Collections.emptyList();
    }

}
