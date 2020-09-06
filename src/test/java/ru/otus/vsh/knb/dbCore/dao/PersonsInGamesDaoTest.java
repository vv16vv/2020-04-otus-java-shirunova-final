package ru.otus.vsh.knb.dbCore.dao;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Test;
import ru.otus.vsh.knb.dbCore.model.PersonsInGames;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class PersonsInGamesDaoTest extends BaseDaoTest {

    @Test
    void searchUnexistingPersonsInGames() {
        sessionManagerHibernate.beginSession();
        Optional<PersonsInGames> game = personsInGamesDaoHibernate.findById(100500);
        sessionManagerHibernate.commitSession();

        assertThat(game).isEmpty();
    }

    @Test
    void createAndSearchNewPersonInGameInsert() {
        val game = getNewGame();
        val person1InGame = getNewPerson1InGame(game);

        sessionManagerHibernate.beginSession();
        gameDaoHibernate.insert(game);
        val newPersonsInGamesId = personsInGamesDaoHibernate.insert(person1InGame);
        sessionManagerHibernate.commitSession();

        sessionManagerHibernate.beginSession();
        val newPersonInGame = personsInGamesDaoHibernate.findById(newPersonsInGamesId);
        sessionManagerHibernate.commitSession();

        assertThat(newPersonsInGamesId).isGreaterThan(0L);
        assertThat(newPersonInGame).isNotEmpty();
        assertThat(newPersonInGame.get()).isEqualTo(person1InGame);
    }

    @Test
    void searchExistingPersonsInGames() {
        val game = getNewGame();
        val person1InGame = getNewPerson1InGame(game);
        val person2InGame = getNewPerson2InGame(game);
        val observerInGame = getNewObserverInGame(game);

        sessionManagerHibernate.beginSession();
        gameDaoHibernate.insert(game);
        personsInGamesDaoHibernate.insert(person1InGame);
        personsInGamesDaoHibernate.insert(person2InGame);
        personsInGamesDaoHibernate.insert(observerInGame);
        sessionManagerHibernate.commitSession();

        sessionManagerHibernate.beginSession();
        val foundPersonsByGame = personsInGamesDaoHibernate.personsByGame(game);
        sessionManagerHibernate.commitSession();
        log.info("Found persons in game {} is {}", game, foundPersonsByGame);

        assertThat(foundPersonsByGame).isNotEmpty();
        assertThat(foundPersonsByGame).hasSize(3);
    }

}
