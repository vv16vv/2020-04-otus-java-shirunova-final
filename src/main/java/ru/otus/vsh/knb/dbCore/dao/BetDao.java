package ru.otus.vsh.knb.dbCore.dao;

import ru.otus.vsh.knb.dbCore.model.Bet;
import ru.otus.vsh.knb.dbCore.model.PersonsInGames;

import java.util.List;

public interface BetDao extends Dao<Bet> {
    List<Bet> findByPersonInGame(PersonsInGames person);
}
