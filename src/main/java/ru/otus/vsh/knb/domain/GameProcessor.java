package ru.otus.vsh.knb.domain;

import ru.otus.vsh.knb.dbCore.model.*;
import ru.otus.vsh.knb.domain.msClient.data.GameData;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GameProcessor {
    Person addNewPlayer(String login, String name, String password);

    Person addNewPlayer(String login, String name, String password, long initialSum);

    Optional<Person> playerByLogin(String login);

    List<Person> players();

    GameSettings getSettings(int items, int turns, int cheats);

    Optional<Game> startNewGame(@Nonnull Person person);

    Optional<Game> startNewGame(@Nonnull Person person, long wager);

    Optional<Game> startNewGame(@Nonnull Person person, @Nonnull GameSettings settings);

    Optional<Game> startNewGame(@Nonnull Person person, @Nonnull GameSettings settings, long wager);

    Optional<Game> gameById(long id);

    List<GameData> gamesToJoinAsPlayer(@Nonnull Person person);

    List<GameData> gamesToJoinAsObserver(@Nonnull Person person);

    GameData joinGameAsPlayer(@Nonnull Game game, @Nonnull Person person);

    GameData joinGameAsObserver(@Nonnull Game game, @Nonnull Person person);

    Person getPlayer1(@Nonnull Game game);

    Optional<Person> getPlayer2(@Nonnull Game game);

    Set<Person> getObservers(@Nonnull Game game);

    Optional<Bet> betForGame(@Nonnull Game game);

    boolean makeBet(@Nonnull Game game, @Nonnull Person person1, @Nonnull Person person2, long wager, @Nonnull EventResults expectedResult);

    boolean isBet(@Nonnull Game game, @Nonnull Person person1, @Nonnull Person person2);

    void endGame(@Nonnull Game game);

    void updatePlayerAccount(@Nonnull Person person);
}
