package ru.otus.vsh.knb.dbCore.hibernate.dao;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import ru.otus.vsh.knb.dbCore.dao.GameDao;
import ru.otus.vsh.knb.dbCore.model.*;
import ru.otus.vsh.knb.dbCore.hibernate.sessionmanager.DatabaseSessionHibernate;
import ru.otus.vsh.knb.dbCore.hibernate.sessionmanager.SessionManagerHibernate;

import javax.annotation.Nonnull;
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

    @Override
    public Game createNewGame(@Nonnull Person person, @Nonnull GameSettings settings, long wager) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getSession();
            val game = Game.builder()
                    .settings(settings)
                    .get();
            hibernateSession.saveOrUpdate(game);
            val pig = PersonsInGames.builder()
                    .game(game)
                    .person(person)
                    .role(Roles.Player1.id())
                    .get();
            val pigDao = new PersonsInGamesDaoHibernate(sessionManager);
            pigDao.insert(pig);

            Bet bet = null;
            if (wager > 0) {
                bet = Bet.builder()
                        .person1(pig)
                        .expectedResult(EventResults.Player1Won.id())
                        .wager(wager)
                        .get();
                val betDao = new BetDaoHibernate(sessionManager);
                betDao.insert(bet);
            }
            return game;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
