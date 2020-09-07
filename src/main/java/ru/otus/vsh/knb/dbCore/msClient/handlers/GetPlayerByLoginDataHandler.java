package ru.otus.vsh.knb.dbCore.msClient.handlers;

import lombok.AllArgsConstructor;
import lombok.val;
import ru.otus.vsh.knb.dbCore.dbService.DBServicePerson;
import ru.otus.vsh.knb.dbCore.msClient.data.GetPlayerByLoginData;
import ru.otus.vsh.knb.dbCore.msClient.data.GetPlayerByLoginReplyData;
import ru.otus.vsh.knb.msCore.common.SimpleReceiveRequestHandler;
import ru.otus.vsh.knb.msCore.message.Message;

import java.util.Optional;

/**
 * Исполняется клиентом [DataBaseMSClient]
 * при обработке сообщения GET_PLAYER_BY_LOGIN
 */
@AllArgsConstructor
public class GetPlayerByLoginDataHandler extends SimpleReceiveRequestHandler<GetPlayerByLoginData, GetPlayerByLoginReplyData> {
    private final DBServicePerson dbServicePlayer;

    @Override
    public Optional<Message<GetPlayerByLoginReplyData>> handle(Message<GetPlayerByLoginData> msg) {
        val login = msg.getBody().getLogin();
        val player = dbServicePlayer.findByLogin(login);
        return Optional.of(Message.buildReplyMessage(msg, new GetPlayerByLoginReplyData(player)));
    }
}
