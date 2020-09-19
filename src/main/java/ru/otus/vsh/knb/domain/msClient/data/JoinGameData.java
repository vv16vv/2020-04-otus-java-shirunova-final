package ru.otus.vsh.knb.domain.msClient.data;

import lombok.Builder;
import lombok.Value;
import ru.otus.vsh.knb.dbCore.model.Person;
import ru.otus.vsh.knb.msCore.common.MessageData;

@Value
@Builder(buildMethodName = "get")
public class JoinGameData implements MessageData {
    Person person;
    boolean isPlayer;
    long gameId;
}
