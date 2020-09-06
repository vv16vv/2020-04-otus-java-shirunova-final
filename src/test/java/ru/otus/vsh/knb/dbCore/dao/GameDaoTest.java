package ru.otus.vsh.knb.dbCore.dao;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Test;
import ru.otus.vsh.knb.dbCore.model.EventResults;
import ru.otus.vsh.knb.dbCore.model.Game;
import ru.otus.vsh.knb.dbCore.model.GameSettings;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class GameDaoTest extends BaseDaoTest {

    @Test
    void searchUnexistingGame() {
        sessionManagerHibernate.beginSession();
        Optional<Game> game = gameDaoHibernate.findById(100500);
        sessionManagerHibernate.commitSession();

        assertThat(game).isEmpty();
    }

    @Test
    void createNewGameInsert() {
        val game = getNewGame();

        sessionManagerHibernate.beginSession();
        val newGameId = gameDaoHibernate.insert(game);
        sessionManagerHibernate.commitSession();

        assertThat(newGameId).isGreaterThan(0L);
        assertThat(game.getSettings().getId()).isGreaterThan(0L);
    }

    @Test
    void searchExistingGame() {
        val game = getNewGame();

        sessionManagerHibernate.beginSession();
        val newGameId = gameDaoHibernate.insert(game);
        sessionManagerHibernate.commitSession();
        log.info("Game is created with id #{}", newGameId);

        sessionManagerHibernate.beginSession();
        val foundGame = gameDaoHibernate.findById(newGameId);
        sessionManagerHibernate.commitSession();
        log.info("Found game is {}", foundGame);

        assertThat(foundGame).isPresent();
        assertThat(foundGame.get()).isEqualTo(game);
    }

    @Test
    void editGameUpdate() {
        val game = getNewGame();

        sessionManagerHibernate.beginSession();
        gameDaoHibernate.insert(game);
        sessionManagerHibernate.commitSession();

        game.setActualResult(EventResults.Player1Won.id());
        sessionManagerHibernate.beginSession();
        gameDaoHibernate.update(game);
        sessionManagerHibernate.commitSession();

        sessionManagerHibernate.beginSession();
        val editedGame = gameDaoHibernate.findById(game.getId());
        sessionManagerHibernate.commitSession();

        assertThat(editedGame).isPresent();
        assertThat(editedGame.get().getActualResult()).isEqualTo(game.getActualResult());
    }

    @Test
    void editGameInsertOrUpdate() {
        val game = getNewGame();

        sessionManagerHibernate.beginSession();
        gameDaoHibernate.insert(game);
        sessionManagerHibernate.commitSession();

        game.setCompleted(true);
        sessionManagerHibernate.beginSession();
        gameDaoHibernate.insertOrUpdate(game);
        sessionManagerHibernate.commitSession();

        sessionManagerHibernate.beginSession();
        val editedGame = gameDaoHibernate.findById(game.getId());
        sessionManagerHibernate.commitSession();

        assertThat(editedGame).isPresent();
        assertThat(editedGame.get().isCompleted()).isEqualTo(game.isCompleted());
    }

}
