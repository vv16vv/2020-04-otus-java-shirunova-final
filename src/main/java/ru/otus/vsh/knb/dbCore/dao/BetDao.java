package ru.otus.vsh.knb.dbCore.dao;

import ru.otus.vsh.knb.dbCore.model.Bet;
import ru.otus.vsh.knb.dbCore.model.PersonsInGames;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public interface BetDao extends Dao<Bet> {
    List<Bet> findByPersonInGame(@Nonnull PersonsInGames person);

    Optional<Bet> findCommonBet(@Nonnull PersonsInGames person1, @Nonnull PersonsInGames person2);

    Optional<Bet> findOpeningBet(@Nonnull PersonsInGames person1);
}
