package ru.otus.vsh.knb.domain.msClient.data;

import lombok.Value;
import ru.otus.vsh.knb.dbCore.model.Person;
import ru.otus.vsh.knb.msCore.common.MessageData;

import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@Value
public class GetPlayerByLoginReplyData implements MessageData {
    Optional<Person> player;
}
