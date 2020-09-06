package ru.otus.vsh.knb.dbCore.dbService;

import ru.otus.vsh.knb.dbCore.dbService.api.DBService;
import ru.otus.vsh.knb.dbCore.model.Game;

import java.util.List;

public interface DBServiceGame extends DBService<Game> {
    List<Game> activeGames();
}
