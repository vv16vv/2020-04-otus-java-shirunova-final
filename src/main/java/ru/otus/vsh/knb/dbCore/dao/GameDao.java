package ru.otus.vsh.knb.dbCore.dao;

import ru.otus.vsh.knb.dbCore.model.Game;

import java.util.List;

public interface GameDao extends Dao<Game> {
    List<Game> activeGames();
}
