package ru.otus.vsh.knb.domain.msClient.data;

import lombok.Builder;
import lombok.Value;
import ru.otus.vsh.knb.dbCore.model.Game;
import ru.otus.vsh.knb.dbCore.model.Person;
import ru.otus.vsh.knb.msCore.common.MessageData;

import java.util.Set;

@Value
@Builder(buildMethodName = "get")
public class EndGameData implements MessageData {
    Game game;
    Person player1;
    Person player2;
    Set<Person> observers;
}
