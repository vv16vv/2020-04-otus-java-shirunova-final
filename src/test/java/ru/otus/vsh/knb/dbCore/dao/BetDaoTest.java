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
    void searchExistingBetByPigs() {
        val game = getNewGame();
        val person1InGame = getNewPerson1InGame(game);
        val person2InGame = getNewPerson2InGame(game);
        val person3InGame = getNewObserverInGame(game);
        val bet1 = getNewBet(person1InGame, person2InGame);
        val bet2 = getNewBet(person2InGame, person3InGame);

        sessionManagerHibernate.beginSession();
        gameDaoHibernate.insert(game);
        personsInGamesDaoHibernate.insert(person1InGame);
        personsInGamesDaoHibernate.insert(person2InGame);
        personsInGamesDaoHibernate.insert(person3InGame);
        betDaoHibernate.insert(bet1);
        betDaoHibernate.insert(bet2);
        sessionManagerHibernate.commitSession();

        sessionManagerHibernate.beginSession();
        val commonBets1 = betDaoHibernate.findCommonBet(person1InGame, person2InGame);
        val commonBets2 = betDaoHibernate.findCommonBet(person2InGame, person1InGame);
        val commonBets3 = betDaoHibernate.findCommonBet(person3InGame, person2InGame);
        val commonBets4 = betDaoHibernate.findCommonBet(person3InGame, person1InGame);
        sessionManagerHibernate.commitSession();
        log.info("Found bets made by persons {} and {} is {}",
                person1InGame.getPerson().getLogin(),
                person2InGame.getPerson().getLogin(),
                commonBets1);
        log.info("Found bets made by persons {} and {} is {}",
                person2InGame.getPerson().getLogin(),
                person1InGame.getPerson().getLogin(),
                commonBets2);
        log.info("Found bets made by persons {} and {} is {}",
                person3InGame.getPerson().getLogin(),
                person2InGame.getPerson().getLogin(),
                commonBets3);
        log.info("Found bets made by persons {} and {} is {}",
                person3InGame.getPerson().getLogin(),
                person1InGame.getPerson().getLogin(),
                commonBets4);

        assertThat(commonBets1).isNotEmpty();
        assertThat(commonBets2).isNotEmpty();
        assertThat(commonBets3).isNotEmpty();
        assertThat(commonBets4).isEmpty();
        assertThat(commonBets1).isEqualTo(commonBets2);
        assertThat(commonBets1).isNotEqualTo(commonBets3);
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
