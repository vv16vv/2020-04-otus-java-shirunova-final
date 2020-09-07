package ru.otus.vsh.knb.dbCore.msClient.data;

import lombok.Value;
import ru.otus.vsh.knb.msCore.common.MessageData;

@Value
public class GetPlayerByLoginData implements MessageData {
    String login;
}
