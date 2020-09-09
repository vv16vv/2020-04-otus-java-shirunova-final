package ru.otus.vsh.knb.domain;

import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;
import ru.otus.vsh.knb.dbCore.dbService.DBServiceBet;
import ru.otus.vsh.knb.dbCore.dbService.DBServiceGame;
import ru.otus.vsh.knb.dbCore.dbService.DBServicePerson;
import ru.otus.vsh.knb.dbCore.dbService.DBServicePersonsInGames;
import ru.otus.vsh.knb.dbCore.model.*;
import ru.otus.vsh.knb.dbCore.msClient.data.GameData;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Component
public class GameProcessorImpl implements GameProcessor {
    private final DBServicePerson dbServicePerson;
    private final DBServiceGame dbServiceGame;
    private final DBServicePersonsInGames dbServicePersonsInGames;
    private final DBServiceBet dbServiceBet;


    @Override
    public Person addNewPlayer(String login, String name, String password) {
        return addNewPlayer(login, name, password, DefaultValues.INITIAL_SUM);
    }

    @Override
    public Person addNewPlayer(String login, String name, String password, long initialSum) {
        val person = dbServicePerson
                .findByLogin(login)
                .orElse(Person.builder()
                        .login(login)
                        .name(name)
                        .password(password)
                        .account(Account.builder().sum(initialSum).get())
                        .get());
        dbServicePerson.saveObject(person);
        return person;
    }

    @Override
    public Optional<Person> playerByLogin(String login) {
        return dbServicePerson.findByLogin(login);
    }

    @Override
    public List<Person> players() {
        return dbServicePerson.findAll();
    }

    @Override
    public Optional<Game> startNewGame(@Nonnull Person person) {
        return startNewGame(person, GameSettings.builder().get(), 0L);
    }

    @Override
    public Optional<Game> startNewGame(@Nonnull Person person, long wager) {
        return startNewGame(person, GameSettings.builder().get(), wager);
    }

    @Override
    public Optional<Game> startNewGame(@Nonnull Person person, @Nonnull GameSettings settings) {
        return startNewGame(person, settings, 0L);
    }

    @Override
    public Optional<Game> startNewGame(@Nonnull Person person, @Nonnull GameSettings settings, long wager) {
        if (person.getAccount().getSum() < wager) return Optional.empty();
        return Optional.of(dbServiceGame.createNewGame(person, settings, wager));
    }

    @Override
    public List<GameData> gamesToJoinAsPlayer(@Nonnull Person person) {
        return gamesToJoin(person, Roles.Player2);
    }

    @Override
    public List<GameData> gamesToJoinAsObserver(@Nonnull Person person) {
        return gamesToJoin(person, Roles.Observer);
    }

    @Nonnull
    private List<GameData> gamesToJoin(@Nonnull Person person, @Nonnull Roles role) {
        if (role == Roles.Player1) throw new GameException("Cannot join to the existing game as a first player");
        val activeGames = dbServiceGame.activeGames();
        val games = new ArrayList<GameData>();
        for (final Game aGame : activeGames) {
            val pigs = dbServicePersonsInGames.personsByGame(aGame);
            val player1 = pigs.stream()
                    .filter(pig -> pig.getRole().equals(Roles.Player1))
                    .findFirst()
                    .orElseThrow(() -> new GameException(String.format("An opened game without a first player %s", aGame)));
            // we cannot add person to the game where they participate already
            if (player1.getPerson().equals(person)) continue;

            val player2 = pigs.stream()
                    .filter(pig -> pig.getRole().equals(Roles.Player2))
                    .findFirst();

            if (role == Roles.Player2) {
                // we cannot join as a second player if their place has been taken already
                if (player2.isPresent()) continue;
            } else {
                // as a observer we cannot join to any game, if they has been participated already as a second player.
                if (player2.isPresent() && player2.get().getPerson().equals(person)) continue;
            }

            val data = GameData.builder()
                    .game(aGame)
                    .player1(player1.getPerson());

            long bet = 0L;
            if (player2.isEmpty()) {
                val bets = dbServiceBet.findByPersonInGame(player1);
                if (bets.isEmpty()) {
                    bet = 0;
                } else {
                    bet = dbServiceBet.findByPersonInGame(player1).get(0).getWager();
                }
            } else {
                bet = dbServiceBet
                        .findCommonBet(player1, player2.get())
                        .map(Bet::getWager)
                        .orElse(0L);
                data.player2(player2.get().getPerson());
            }
            data.wager(bet);

            games.add(data.get());
        }
        return games;
    }

    @Override
    public boolean joinGameAsPlayer(@Nonnull Game game, @Nonnull Person person) {
        val bet = betForGame(game);
        if (bet.isPresent() && !person.canAfford(bet.get().getWager())) return false;
        dbServicePersonsInGames.joinGame(game, person, Roles.Player2);
        return true;
    }

    @Override
    public void joinGameAsObserver(@Nonnull Game game, @Nonnull Person person) {
        dbServicePersonsInGames.joinGame(game, person, Roles.Observer);
    }

    @Override
    public Optional<Bet> betForGame(@Nonnull Game game) {
        val pig1 = dbServicePersonsInGames.personByGameAndRolePlayer1(game).orElseThrow();
        val pig2 = dbServicePersonsInGames.personByGameAndRolePlayer2(game);
        return dbServiceBet.findGameBet(pig1, pig2);
    }

    @Override
    public boolean makeBet(@Nonnull Game game, @Nonnull Person person1, @Nonnull Person person2, long wager, @Nonnull EventResults expectedResult) {
        if (!person1.canAfford(wager) || !person2.canAfford(wager)) return false;
        assert expectedResult != EventResults.Draw && expectedResult != EventResults.Unknown;
        val pig1 = dbServicePersonsInGames.personByGameAndPlayer(game, person1);
        val pig2 = dbServicePersonsInGames.personByGameAndPlayer(game, person2);
        // These persons don't participate in the game
        if (pig1.isEmpty() || pig2.isEmpty()) return false;
        return dbServiceBet.makeBet(pig1.get(), pig2.get(), wager, expectedResult);
    }

    @Override
    public boolean isBet(@Nonnull Game game, @Nonnull Person person1, @Nonnull Person person2) {
        val pig1 = dbServicePersonsInGames.personByGameAndPlayer(game, person1);
        val pig2 = dbServicePersonsInGames.personByGameAndPlayer(game, person2);
        if (pig1.isEmpty()) return false;
        return pig2
                .map(personsInGames -> dbServiceBet.findCommonBet(pig1.get(), personsInGames).isPresent())
                .orElseGet(() -> dbServiceBet.findGameBet(pig1.get(), pig2).isPresent());
    }
}