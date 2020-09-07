package ru.otus.vsh.knb.dbCore.msClient.handlers;

import lombok.AllArgsConstructor;
import lombok.val;
import ru.otus.vsh.knb.dbCore.dbService.DBServicePerson;
import ru.otus.vsh.knb.dbCore.msClient.data.NewPlayerData;
import ru.otus.vsh.knb.dbCore.msClient.data.NewPlayerReplyData;
import ru.otus.vsh.knb.dbCore.model.Person;
import ru.otus.vsh.knb.msCore.common.SimpleReceiveRequestHandler;
import ru.otus.vsh.knb.msCore.message.Message;

import java.util.Optional;

/**
 * Исполняется клиентом [DataBaseMSClient]
 * при обработке сообщения NEW_PLAYER
 */
@AllArgsConstructor
public class NewPlayerDataHandler extends SimpleReceiveRequestHandler<NewPlayerData, NewPlayerReplyData> {
    private final DBServicePerson dbServicePlayer;

    @Override
    public Optional<Message<NewPlayerReplyData>> handle(Message<NewPlayerData> msg) {
        val existPlayer = dbServicePlayer.findByLogin(msg.getBody().getLogin());
        var result = existPlayer.isEmpty();
        if (existPlayer.isEmpty()) {
            val newPlayer = Person.builder()
                    .login(msg.getBody().getLogin())
                    .name(msg.getBody().getName())
                    .password(msg.getBody().getPassword())
                    .id(0)
                    .get();
            val newId = dbServicePlayer.newObject(newPlayer);
            result = newId > 0;
        }
        return Optional.of(Message.buildReplyMessage(msg, new NewPlayerReplyData(result)));
    }
}
