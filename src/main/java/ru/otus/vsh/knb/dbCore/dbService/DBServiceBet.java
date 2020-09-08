package ru.otus.vsh.knb.dbCore.dbService;

import ru.otus.vsh.knb.dbCore.dbService.api.DBService;
import ru.otus.vsh.knb.dbCore.model.Bet;
import ru.otus.vsh.knb.dbCore.model.PersonsInGames;

import java.util.List;

public interface DBServiceBet extends DBService<Bet> {
    List<Bet> findByPersonInGame(PersonsInGames person);
    List<Bet> findCommonBets(PersonsInGames person1, PersonsInGames person2);
}
