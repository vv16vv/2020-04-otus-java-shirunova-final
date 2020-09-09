package ru.otus.vsh.knb.domain;

import lombok.val;
import org.junit.jupiter.api.Test;
import ru.otus.vsh.knb.dbCore.dao.BaseDaoTest;
import ru.otus.vsh.knb.dbCore.model.EventResults;
import ru.otus.vsh.knb.dbCore.model.GameSettings;
import ru.otus.vsh.knb.dbCore.model.Roles;
import ru.otus.vsh.knb.webCore.lobbyPage.AvailGameStyles;

import static org.assertj.core.api.Assertions.assertThat;

public class GameProcessorTest extends BaseDaoTest {
    private static final String firstLogin = "aa";
    private static final String firstName = "A a";
    private static final String firstPassword = "12345";
    private static final String secondLogin = "bb";
    private static final String secondName = "B b";
    private static final String secondPassword = "54321";
    private static final String thirdLogin = "cc";
    private static final String thirdName = "C c";
    private static final String thirdPassword = "12121";
    private static final String forthLogin = "dd";
    private static final String forthName = "D d";
    private static final String forthPassword = "34343";

    @Test
    public void addAndSearchNewPlayer() {
        val newPlayer = gameProcessor.addNewPlayer(firstLogin, firstName, firstPassword);

        assertThat(newPlayer.getId()).isGreaterThan(0);
        assertThat(newPlayer.getPassword()).isEqualTo(firstPassword);
        assertThat(newPlayer.getAccount().getSum()).isEqualTo(DefaultValues.INITIAL_SUM);

        val foundPlayer = gameProcessor.playerByLogin(firstLogin);
        assertThat(foundPlayer).isNotEmpty();
        assertThat(foundPlayer.get()).isEqualTo(newPlayer);
    }

    @Test
    public void allPlayers() {
        val newPlayer = gameProcessor.addNewPlayer(firstLogin, firstName, firstPassword);
        val players = gameProcessor.players();

        assertThat(players).hasSize(1);
        assertThat(players.get(0)).isEqualTo(newPlayer);
    }

    @Test
    public void createNewSimpleGameWithoutWager() {
        val firstPlayer = gameProcessor.addNewPlayer(firstLogin, firstName, firstPassword);
        val game = gameProcessor.startNewGame(firstPlayer);

        assertThat(game).isPresent();

        assertThat(game.get().getId()).isGreaterThan(0L);
        assertThat(game.get().getActualResult()).isEqualTo(EventResults.Unknown);
        assertThat(game.get().isCompleted()).isFalse();

        val settings = game.get().getSettings();
        assertThat(settings.getNumberOfItems()).isEqualTo(DefaultValues.SIMPLE_SETTINGS_ITEMS);
        assertThat(settings.getNumberOfTurns()).isEqualTo(DefaultValues.SIMPLE_SETTINGS_TURNS);
        assertThat(settings.getNumberOfCheats()).isEqualTo(DefaultValues.SIMPLE_SETTINGS_CHEATS);

        val pigs = dbServicePersonsInGames.personsByGame(game.get());
        assertThat(pigs).hasSize(1);
        assertThat(pigs.get(0).getPerson()).isEqualTo(firstPlayer);
        assertThat(pigs.get(0).getRole()).isEqualTo(Roles.Player1);

        val pig1 = dbServicePersonsInGames.personByGameAndRolePlayer1(game.get());
        assertThat(pig1).isPresent();
        assertThat(pig1.get()).isEqualTo(pigs.get(0));

        val pig2 = dbServicePersonsInGames.personByGameAndPlayer(game.get(), firstPlayer);
        assertThat(pig2).isPresent();
        assertThat(pig2.get()).isEqualTo(pig1.get());

        val bets = dbServiceBet.findByPersonInGame(pig1.get());
        assertThat(bets).hasSize(0);
    }

    @Test
    public void createNewSimpleGameWithWager() {
        val wager = 100L;
        val moneyAfterBet = DefaultValues.INITIAL_SUM - wager;
        val firstPlayer = gameProcessor.addNewPlayer(firstLogin, firstName, firstPassword);
        val game = gameProcessor.startNewGame(firstPlayer, wager);

        assertThat(game).isPresent();

        assertThat(game.get().getId()).isGreaterThan(0L);
        assertThat(game.get().getActualResult()).isEqualTo(EventResults.Unknown);
        assertThat(game.get().isCompleted()).isFalse();

        val pig1 = dbServicePersonsInGames.personByGameAndRolePlayer1(game.get());
        assertThat(pig1).isPresent();

        val bets = dbServiceBet.findByPersonInGame(pig1.get());
        assertThat(bets).hasSize(1);
        assertThat(bets.get(0).getWager()).isEqualTo(wager);
        assertThat(bets.get(0).getExpectedResult()).isEqualTo(EventResults.Player1Won);
        assertThat(bets.get(0).getPerson2()).isNull();

        assertThat(firstPlayer.getAccount().getSum()).isEqualTo(moneyAfterBet);
    }

    @Test
    public void createNewCustomGameWithWager() {
        val wager = 100L;
        val items = 13;
        val turns = 50;
        val cheats = 5;
        val moneyAfterBet = DefaultValues.INITIAL_SUM - wager;

        val firstPlayer = gameProcessor.addNewPlayer(firstLogin, firstName, firstPassword);
        val settings = GameSettings.builder()
                .numberOfCheats(cheats)
                .numberOfItems(items)
                .numberOfTurns(turns)
                .get();

        val game = gameProcessor.startNewGame(firstPlayer, settings, wager);

        assertThat(game).isPresent();

        assertThat(game.get().getId()).isGreaterThan(0L);
        assertThat(game.get().getActualResult()).isEqualTo(EventResults.Unknown);
        assertThat(game.get().getSettings()).isEqualTo(settings);
        assertThat(game.get().isCompleted()).isFalse();

        val pig1 = dbServicePersonsInGames.personByGameAndRolePlayer1(game.get());
        assertThat(pig1).isPresent();

        val bets = dbServiceBet.findByPersonInGame(pig1.get());
        assertThat(bets).hasSize(1);
        assertThat(bets.get(0).getWager()).isEqualTo(wager);
        assertThat(bets.get(0).getExpectedResult()).isEqualTo(EventResults.Player1Won);
        assertThat(bets.get(0).getPerson2()).isNull();

        assertThat(firstPlayer.getAccount().getSum()).isEqualTo(moneyAfterBet);
    }

    @Test
    public void createNewCustomGameWithoutWager() {
        val items = 13;
        val turns = 50;
        val cheats = 5;

        val firstPlayer = gameProcessor.addNewPlayer(firstLogin, firstName, firstPassword);
        val settings = GameSettings.builder()
                .numberOfCheats(cheats)
                .numberOfItems(items)
                .numberOfTurns(turns)
                .get();

        val game = gameProcessor.startNewGame(firstPlayer, settings);

        assertThat(game).isPresent();

        assertThat(game.get().getId()).isGreaterThan(0L);
        assertThat(game.get().getActualResult()).isEqualTo(EventResults.Unknown);
        assertThat(game.get().getSettings()).isEqualTo(settings);
        assertThat(game.get().isCompleted()).isFalse();

        val pig1 = dbServicePersonsInGames.personByGameAndRolePlayer1(game.get());
        assertThat(pig1).isPresent();

        val bets = dbServiceBet.findByPersonInGame(pig1.get());
        assertThat(bets).hasSize(0);

        assertThat(firstPlayer.getAccount().getSum()).isEqualTo(DefaultValues.INITIAL_SUM);
    }

    @Test
    public void gamesToJoinAsPlayer() {
        val firstPlayer = gameProcessor.addNewPlayer(firstLogin, firstName, firstPassword);
        val secondPlayer = gameProcessor.addNewPlayer(secondLogin, secondName, secondPassword);

        val game = gameProcessor.startNewGame(firstPlayer);
        assertThat(game).isPresent();

        val gamesForSecondPlayer = gameProcessor.gamesToJoinAsPlayer(secondPlayer);
        assertThat(gamesForSecondPlayer).hasSize(1);
        assertThat(gamesForSecondPlayer.get(0).getGame()).isEqualTo(game.get());
        assertThat(gamesForSecondPlayer.get(0).getPlayer1()).isEqualTo(firstPlayer);
        assertThat(gamesForSecondPlayer.get(0).getPlayer2()).isNull();
        assertThat(gamesForSecondPlayer.get(0).getWager()).isEqualTo(0L);
        assertThat(gamesForSecondPlayer.get(0).style(secondPlayer)).isEqualTo(AvailGameStyles.AVAILABLE_TO_PLAY);

        val gamesForFirstPlayer = gameProcessor.gamesToJoinAsPlayer(firstPlayer);
        assertThat(gamesForFirstPlayer).hasSize(0);
    }

    @Test
    public void joinGameAsPlayer() {
        val firstPlayer = gameProcessor.addNewPlayer(firstLogin, firstName, firstPassword);
        val secondPlayer = gameProcessor.addNewPlayer(secondLogin, secondName, secondPassword);

        val game = gameProcessor.startNewGame(firstPlayer);
        assertThat(game).isPresent();

        val result = gameProcessor.joinGameAsPlayer(game.get(), secondPlayer);
        assertThat(result).isTrue();

        val pigs = dbServicePersonsInGames.personsByGame(game.get());
        assertThat(pigs).hasSize(2);

        val pig1 = dbServicePersonsInGames.personByGameAndRolePlayer2(game.get());
        assertThat(pig1).isPresent();
        assertThat(pig1.get().getPerson()).isEqualTo(secondPlayer);

        val pig2 = dbServicePersonsInGames.personByGameAndPlayer(game.get(), secondPlayer);
        assertThat(pig2).isPresent();
        assertThat(pig2.get().getRole()).isEqualTo(Roles.Player2);
        assertThat(pig2.get()).isEqualTo(pig1.get());

        val gameBet = gameProcessor.betForGame(game.get());
        assertThat(gameBet).isEmpty();

        assertThat(firstPlayer.getAccount().getSum()).isEqualTo(DefaultValues.INITIAL_SUM);
        assertThat(secondPlayer.getAccount().getSum()).isEqualTo(DefaultValues.INITIAL_SUM);

    }

    @Test
    public void joinGameWithWagerAsPlayer() {
        val wager = 100L;
        val moneyAfterBet = DefaultValues.INITIAL_SUM - wager;
        val firstPlayer = gameProcessor.addNewPlayer(firstLogin, firstName, firstPassword);
        val secondPlayer = gameProcessor.addNewPlayer(secondLogin, secondName, secondPassword);

        val game = gameProcessor.startNewGame(firstPlayer, wager);
        assertThat(game).isPresent();

        val result = gameProcessor.joinGameAsPlayer(game.get(), secondPlayer);
        assertThat(result).isTrue();

        val pigs = dbServicePersonsInGames.personsByGame(game.get());
        assertThat(pigs).hasSize(2);

        val pig1 = dbServicePersonsInGames.personByGameAndRolePlayer2(game.get());
        assertThat(pig1).isPresent();
        assertThat(pig1.get().getPerson()).isEqualTo(secondPlayer);

        val pig2 = dbServicePersonsInGames.personByGameAndPlayer(game.get(), secondPlayer);
        assertThat(pig2).isPresent();
        assertThat(pig2.get().getRole()).isEqualTo(Roles.Player2);
        assertThat(pig2.get()).isEqualTo(pig1.get());

        val bets1 = dbServiceBet.findByPersonInGame(pig1.get());
        assertThat(bets1).hasSize(1);
        assertThat(bets1.get(0).getWager()).isEqualTo(wager);
        assertThat(bets1.get(0).getExpectedResult()).isEqualTo(EventResults.Player1Won);
        assertThat(bets1.get(0).getPerson2()).isEqualTo(pig1.get());

        val gameBet = gameProcessor.betForGame(game.get());
        assertThat(gameBet).isPresent();
        assertThat(gameBet.get()).isEqualTo(bets1.get(0));
        assertThat(gameBet.get().getWager()).isEqualTo(wager);

        assertThat(firstPlayer.getAccount().getSum()).isEqualTo(moneyAfterBet);
        assertThat(secondPlayer.getAccount().getSum()).isEqualTo(moneyAfterBet);

    }

    @Test
    public void gamesToJoinAsObserverWithoutSndPlayer() {
        val wager = 100L;

        val firstPlayer = gameProcessor.addNewPlayer(firstLogin, firstName, firstPassword);
        val secondPlayer = gameProcessor.addNewPlayer(secondLogin, secondName, secondPassword);
        val thirdPlayer = gameProcessor.addNewPlayer(thirdLogin, thirdName, thirdPassword);

        val game = gameProcessor.startNewGame(firstPlayer, wager);
        assertThat(game).isPresent();

        val gamesForThirdPlayer = gameProcessor.gamesToJoinAsObserver(thirdPlayer);
        assertThat(gamesForThirdPlayer).hasSize(1);
        assertThat(gamesForThirdPlayer.get(0).getGame()).isEqualTo(game.get());
        assertThat(gamesForThirdPlayer.get(0).getPlayer1()).isEqualTo(firstPlayer);
        assertThat(gamesForThirdPlayer.get(0).getPlayer2()).isNull();
        assertThat(gamesForThirdPlayer.get(0).getWager()).isEqualTo(wager);
        assertThat(gamesForThirdPlayer.get(0).style(secondPlayer)).isEqualTo(AvailGameStyles.AVAILABLE_TO_PLAY);

        val gamesForFirstPlayer = gameProcessor.gamesToJoinAsObserver(firstPlayer);
        assertThat(gamesForFirstPlayer).hasSize(0);
    }

    @Test
    public void gamesToJoinAsObserverWithSndPlayer() {
        val wager = 100L;

        val firstPlayer = gameProcessor.addNewPlayer(firstLogin, firstName, firstPassword);
        val secondPlayer = gameProcessor.addNewPlayer(secondLogin, secondName, secondPassword);
        val thirdPlayer = gameProcessor.addNewPlayer(thirdLogin, thirdName, thirdPassword);

        val game = gameProcessor.startNewGame(firstPlayer, wager);
        assertThat(game).isPresent();

        gameProcessor.joinGameAsPlayer(game.get(), secondPlayer);

        val gamesForThirdPlayer = gameProcessor.gamesToJoinAsObserver(thirdPlayer);
        assertThat(gamesForThirdPlayer).hasSize(1);
        assertThat(gamesForThirdPlayer.get(0).getGame()).isEqualTo(game.get());
        assertThat(gamesForThirdPlayer.get(0).getPlayer1()).isEqualTo(firstPlayer);
        assertThat(gamesForThirdPlayer.get(0).getPlayer2()).isEqualTo(secondPlayer);
        assertThat(gamesForThirdPlayer.get(0).getWager()).isEqualTo(wager);
        assertThat(gamesForThirdPlayer.get(0).style(secondPlayer)).isEqualTo(AvailGameStyles.AVAILABLE_TO_OBSERVE);

        val gamesForFirstPlayer = gameProcessor.gamesToJoinAsObserver(firstPlayer);
        assertThat(gamesForFirstPlayer).hasSize(0);
    }

    @Test
    public void joinGameWithSndPlayerAsObserver() {
        val wager = 100L;

        val firstPlayer = gameProcessor.addNewPlayer(firstLogin, firstName, firstPassword);
        val secondPlayer = gameProcessor.addNewPlayer(secondLogin, secondName, secondPassword);
        val thirdPlayer = gameProcessor.addNewPlayer(thirdLogin, thirdName, thirdPassword);

        val game = gameProcessor.startNewGame(firstPlayer, wager);
        assertThat(game).isPresent();

        gameProcessor.joinGameAsPlayer(game.get(), secondPlayer);

        gameProcessor.joinGameAsObserver(game.get(), thirdPlayer);

        val pigs = dbServicePersonsInGames.personsByGame(game.get());
        assertThat(pigs).hasSize(3);

        val pigsObservers = dbServicePersonsInGames.personByGameAndRoleObserver(game.get());
        assertThat(pigsObservers).hasSize(1);
        assertThat(pigsObservers.get(0).getPerson()).isEqualTo(thirdPlayer);

        val pig2 = dbServicePersonsInGames.personByGameAndPlayer(game.get(), thirdPlayer);
        assertThat(pig2).isPresent();
        assertThat(pig2.get().getRole()).isEqualTo(Roles.Observer);
        assertThat(pig2.get()).isEqualTo(pigsObservers.get(0));

        val bets1 = dbServiceBet.findByPersonInGame(pigsObservers.get(0));
        assertThat(bets1).hasSize(0);

        assertThat(thirdPlayer.getAccount().getSum()).isEqualTo(DefaultValues.INITIAL_SUM);
    }

    @Test
    public void makeBetBtwObservers() {
        val wager = 100L;
        val moneyAfterBet = DefaultValues.INITIAL_SUM - wager;

        val firstPlayer = gameProcessor.addNewPlayer(firstLogin, firstName, firstPassword);
        val secondPlayer = gameProcessor.addNewPlayer(secondLogin, secondName, secondPassword);
        val thirdPlayer = gameProcessor.addNewPlayer(thirdLogin, thirdName, thirdPassword);
        val forthPlayer = gameProcessor.addNewPlayer(forthLogin, forthName, forthPassword);

        val game = gameProcessor.startNewGame(firstPlayer);
        assertThat(game).isPresent();

        gameProcessor.joinGameAsPlayer(game.get(), secondPlayer);
        gameProcessor.joinGameAsObserver(game.get(), thirdPlayer);
        gameProcessor.joinGameAsObserver(game.get(), forthPlayer);

        val result = gameProcessor.makeBet(game.get(), thirdPlayer, forthPlayer, wager, EventResults.Player1Won);
        assertThat(result).isTrue();

        val pigs = dbServicePersonsInGames.personsByGame(game.get());
        assertThat(pigs).hasSize(4);

        val pigsObservers = dbServicePersonsInGames.personByGameAndRoleObserver(game.get());
        assertThat(pigsObservers).hasSize(2);

        val pig3 = dbServicePersonsInGames.personByGameAndPlayer(game.get(), thirdPlayer);
        assertThat(pig3).isPresent();
        assertThat(pig3.get().getRole()).isEqualTo(Roles.Observer);

        val pig4 = dbServicePersonsInGames.personByGameAndPlayer(game.get(), forthPlayer);
        assertThat(pig4).isPresent();
        assertThat(pig4.get().getRole()).isEqualTo(Roles.Observer);

        val bet = dbServiceBet.findCommonBet(pig3.get(), pig4.get());
        assertThat(bet).isPresent();
        assertThat(bet.get().getWager()).isEqualTo(wager);
        assertThat(bet.get().getExpectedResult()).isEqualTo(EventResults.Player1Won);

        val updatedThirdPlayer = bet.get().getPerson1().getPerson();
        val updatedForthPlayer = bet.get().getPerson2().getPerson();

        assertThat(updatedThirdPlayer.getAccount().getSum()).isEqualTo(moneyAfterBet);
        assertThat(updatedForthPlayer.getAccount().getSum()).isEqualTo(moneyAfterBet);

    }
}
