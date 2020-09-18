package ru.otus.vsh.knb.domain.msClient.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.vsh.knb.dbCore.model.Person;
import ru.otus.vsh.knb.msCore.common.MessageData;

@Data
@AllArgsConstructor
public class AvailableGamesForPersonData implements MessageData {
    Person person;
}
