package ru.otus.vsh.knb.dbCore.hibernate.dao;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import ru.otus.vsh.knb.dbCore.dao.PersonsInGamesDao;
import ru.otus.vsh.knb.dbCore.model.Game;
import ru.otus.vsh.knb.dbCore.model.Person;
import ru.otus.vsh.knb.dbCore.model.PersonsInGames;
import ru.otus.vsh.knb.dbCore.model.Roles;
import ru.otus.vsh.knb.dbCore.hibernate.sessionmanager.SessionManagerHibernate;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class PersonsInGamesDaoHibernate extends AbstractDaoHibernate<PersonsInGames> implements PersonsInGamesDao {

    public PersonsInGamesDaoHibernate(SessionManagerHibernate sessionManager) {
        super(sessionManager, PersonsInGames.class);
    }

    @Override
    public List<PersonsInGames> personsByGame(@Nonnull Game game) {
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

    @Override
    public List<PersonsInGames> personByGameAndRole(@Nonnull Game game, @Nonnull Roles role) {
        try {
            var entityManager = sessionManager.getEntityManager();
            return entityManager
                    .createNamedQuery(PersonsInGames.GET_PIG_BY_GAME_AND_ROLE, PersonsInGames.class)
                    .setParameter("gameId", game.getId())
                    .setParameter("role", role.id())
                    .getResultList();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    @Override
    public Optional<PersonsInGames> personByGameAndPlayer(@Nonnull Game game, @Nonnull Person person) {
        try {
            var entityManager = sessionManager.getEntityManager();
            val pig = entityManager
                    .createNamedQuery(PersonsInGames.GET_PIG_BY_GAME_AND_PERSON, PersonsInGames.class)
                    .setParameter("gameId", game.getId())
                    .setParameter("personId", person.getId())
                    .getResultList();
            if (pig.isEmpty()) return Optional.empty();
            else return Optional.of(pig.get(0));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public void joinGame(@Nonnull Game game, @Nonnull Person person, @Nonnull Roles role) {
        val pig = PersonsInGames.builder()
                .game(game)
                .person(person)
                .role(role.id())
                .get();
        insert(pig);
        if (role == Roles.Player2) {
            try {
                val pig1 = personByGameAndRole(game, Roles.Player1).get(0);
                val betDao = new BetDaoHibernate(sessionManager);
                val bet = betDao.findByPersonInGame(pig1)
                        .stream().filter(b -> b.getPerson2() == null)
                        .findFirst();
                bet.ifPresent(value -> {
                    value.setPerson2(pig);
                    person.getAccount().decrease(value.getWager());
                    val personDao = new PersonDaoHibernate(sessionManager);
                    personDao.update(person);
                    betDao.update(value);
                });
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
