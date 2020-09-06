package ru.otus.vsh.knb.dbCore.dbService;

import ru.otus.vsh.knb.dbCore.dbService.api.DBService;
import ru.otus.vsh.knb.dbCore.model.Game;
import ru.otus.vsh.knb.dbCore.model.Person;
import ru.otus.vsh.knb.dbCore.model.PersonsInGames;

import java.util.List;
import java.util.Optional;

public interface DBServicePersonsInGames extends DBService<PersonsInGames> {
    List<PersonsInGames> personsByGame(Game game);
}
