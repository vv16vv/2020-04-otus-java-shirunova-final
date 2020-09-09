package ru.otus.vsh.knb.dbCore.dbService;

import ru.otus.vsh.knb.dbCore.dbService.api.DBService;
import ru.otus.vsh.knb.dbCore.model.Bet;
import ru.otus.vsh.knb.dbCore.model.EventResults;
import ru.otus.vsh.knb.dbCore.model.PersonsInGames;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public interface DBServiceBet extends DBService<Bet> {
    List<Bet> findByPersonInGame(@Nonnull PersonsInGames person);

    Optional<Bet> findCommonBet(@Nonnull PersonsInGames person1, @Nonnull PersonsInGames person2);

    Optional<Bet> findGameBet(@Nonnull PersonsInGames person1, @Nonnull Optional<PersonsInGames> person2);

    boolean makeBet(@Nonnull PersonsInGames person1, @Nonnull PersonsInGames person2, long wager, @Nonnull EventResults expectedResult);
}
