package ru.otus.vsh.knb.hibernate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.otus.vsh.knb.dbCore.dao.GameDao;
import ru.otus.vsh.knb.dbCore.model.Game;
import ru.otus.vsh.knb.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class GameDaoHibernate extends AbstractDaoHibernate<Game> implements GameDao {

    public GameDaoHibernate(SessionManagerHibernate sessionManager) {
        super(sessionManager, Game.class);
    }

    @Override
    public List<Game> activeGames() {
        try {
            var entityManager = sessionManager.getEntityManager();
            return entityManager
                    .createNamedQuery(Game.GET_ACTIVE_GAMES, Game.class)
                    .getResultList();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Collections.emptyList();
    }

}
