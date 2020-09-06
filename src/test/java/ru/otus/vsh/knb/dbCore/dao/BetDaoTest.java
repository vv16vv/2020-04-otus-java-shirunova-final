package ru.otus.vsh.knb.dbCore.dao;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Test;
import ru.otus.vsh.knb.dbCore.model.Bet;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class BetDaoTest extends BaseDaoTest {

    @Test
    void searchUnexistingBet() {
        sessionManagerHibernate.beginSession();
        Optional<Bet> bet = betDaoHibernate.findById(100500);
        sessionManagerHibernate.commitSession();

        assertThat(bet).isEmpty();
    }

    @Test
    void createNewBetInsert() {
        val game = getNewGame();
        val person1InGame = getNewPerson1InGame(game);
        val person2InGame = getNewPerson2InGame(game);
        val bet = getNewBet(person1InGame, person2InGame);

        sessionManagerHibernate.beginSession();
        gameDaoHibernate.insert(game);
        personsInGamesDaoHibernate.insert(person1InGame);
        personsInGamesDaoHibernate.insert(person2InGame);
        val newBetId = betDaoHibernate.insert(bet);
        sessionManagerHibernate.commitSession();

        assertThat(newBetId).isGreaterThan(0L);
    }

    @Test
    void searchExistingBetById() {
        val game = getNewGame();
        val person1InGame = getNewPerson1InGame(game);
        val person2InGame = getNewPerson2InGame(game);
        val bet = getNewBet(person1InGame, person2InGame);

        sessionManagerHibernate.beginSession();
        gameDaoHibernate.insert(game);
        personsInGamesDaoHibernate.insert(person1InGame);
        personsInGamesDaoHibernate.insert(person2InGame);
        val newBetId = betDaoHibernate.insert(bet);
        sessionManagerHibernate.commitSession();
        log.info("Bet is created with id #{} ", newBetId);

        sessionManagerHibernate.beginSession();
        val foundBet = betDaoHibernate.findById(newBetId);
        sessionManagerHibernate.commitSession();
        log.info("Found bet is {}", foundBet);

        assertThat(foundBet).isPresent();
        assertThat(foundBet.get()).isEqualTo(bet);
    }

    @Test
    void searchExistingBetByPig() {
        val game = getNewGame();
        val person1InGame = getNewPerson1InGame(game);
        val person2InGame = getNewPerson2InGame(game);
        val bet1 = getNewBet(person1InGame, person2InGame);
        val bet2 = getNewBet(person2InGame, person1InGame);

        sessionManagerHibernate.beginSession();
        gameDaoHibernate.insert(game);
        personsInGamesDaoHibernate.insert(person1InGame);
        personsInGamesDaoHibernate.insert(person2InGame);
        betDaoHibernate.insert(bet1);
        betDaoHibernate.insert(bet2);
        sessionManagerHibernate.commitSession();

        sessionManagerHibernate.beginSession();
        val person1Bets = betDaoHibernate.findByPersonInGame(person1InGame);
        val person2Bets = betDaoHibernate.findByPersonInGame(person2InGame);
        sessionManagerHibernate.commitSession();
        log.info("Found bets for person {} is {}", person1InGame, person1Bets);
        log.info("Found bets for person {} is {}", person2InGame, person2Bets);

        assertThat(person1Bets).hasSize(2);
        assertThat(person2Bets).hasSize(2);
    }

    @Test
    void updateBet() {
        val game = getNewGame();
        val person1InGame = getNewPerson1InGame(game);
        val person2InGame = getNewPerson2InGame(game);
        val bet = getNewBet(person1InGame, person2InGame);

        sessionManagerHibernate.beginSession();
        gameDaoHibernate.insert(game);
        personsInGamesDaoHibernate.insert(person1InGame);
        personsInGamesDaoHibernate.insert(person2InGame);
        betDaoHibernate.insert(bet);
        sessionManagerHibernate.commitSession();

        bet.setClosed(true);
        sessionManagerHibernate.beginSession();
        betDaoHibernate.update(bet);
        sessionManagerHibernate.commitSession();

        sessionManagerHibernate.beginSession();
        val editedBet = betDaoHibernate.findById(bet.getId());
        sessionManagerHibernate.commitSession();

        assertThat(editedBet).isPresent();
        assertThat(editedBet.get().isClosed()).isTrue();
        assertThat(editedBet.get().isClosed()).isEqualTo(bet.isClosed());
    }

}
