package ru.otus.vsh.knb.domain.msClient.data;

import lombok.Value;
import ru.otus.vsh.knb.dbCore.model.Person;
import ru.otus.vsh.knb.msCore.common.MessageData;

import java.util.List;

@Value
public class PlayersReplyData implements MessageData {
    List<Person> players;
}
