package ru.otus.vsh.knb.domain.msClient.data;

import lombok.AllArgsConstructor;
import lombok.Value;
import ru.otus.vsh.knb.dbCore.model.Person;
import ru.otus.vsh.knb.msCore.common.MessageData;

@Value
@AllArgsConstructor
public class UpdatePersonData implements MessageData {
    Person person;
}
