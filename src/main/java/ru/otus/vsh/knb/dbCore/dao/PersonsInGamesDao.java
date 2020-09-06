package ru.otus.vsh.knb.dbCore.dao;

import ru.otus.vsh.knb.dbCore.model.Game;
import ru.otus.vsh.knb.dbCore.model.PersonsInGames;

import java.util.List;

public interface PersonsInGamesDao extends Dao<PersonsInGames> {
    List<PersonsInGames> personsByGame(Game game);
}
