package ru.otus.vsh.knb.domain.msClient.data;

import lombok.Data;
import ru.otus.vsh.knb.msCore.common.MessageData;

@Data
public class AvailableGamesForPersonData implements MessageData {
    String personLogin;
}
