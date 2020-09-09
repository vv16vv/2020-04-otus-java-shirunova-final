package ru.otus.vsh.knb.domain;

import ru.otus.vsh.knb.dbCore.model.*;
import ru.otus.vsh.knb.dbCore.msClient.data.GameData;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public interface GameProcessor {
    Person addNewPlayer(String login, String name, String password);

    Person addNewPlayer(String login, String name, String password, long initialSum);

    Optional<Person> playerByLogin(String login);

    List<Person> players();

    Optional<Game> startNewGame(@Nonnull Person person);

    Optional<Game> startNewGame(@Nonnull Person person, long wager);

    Optional<Game> startNewGame(@Nonnull Person person, @Nonnull GameSettings settings);

    Optional<Game> startNewGame(@Nonnull Person person, @Nonnull GameSettings settings, long wager);

    List<GameData> gamesToJoinAsPlayer(@Nonnull Person person);

    List<GameData> gamesToJoinAsObserver(@Nonnull Person person);

    boolean joinGameAsPlayer(@Nonnull Game game, @Nonnull Person person);

    void joinGameAsObserver(@Nonnull Game game, @Nonnull Person person);

    Optional<Bet> betForGame(@Nonnull Game game);

    boolean makeBet(@Nonnull Game game, @Nonnull Person person1, @Nonnull Person person2, long wager, @Nonnull EventResults expectedResult);

    boolean isBet(@Nonnull Game game, @Nonnull Person person1, @Nonnull Person person2);
}
