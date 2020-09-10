package ru.otus.vsh.knb.domain.msClient.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.vsh.knb.msCore.common.MessageData;

@Data
@NoArgsConstructor
public class NewPlayerData implements MessageData {
    String login;
    String password;
    String name;
}
