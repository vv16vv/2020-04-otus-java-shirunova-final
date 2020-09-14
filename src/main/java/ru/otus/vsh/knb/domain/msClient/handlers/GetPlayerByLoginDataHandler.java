package ru.otus.vsh.knb.domain.msClient.handlers;

import lombok.AllArgsConstructor;
import lombok.val;
import ru.otus.vsh.knb.domain.GameProcessor;
import ru.otus.vsh.knb.domain.msClient.data.GetPlayerByLoginData;
import ru.otus.vsh.knb.domain.msClient.data.GetPlayerByLoginReplyData;
import ru.otus.vsh.knb.msCore.common.SimpleReceiveRequestHandler;
import ru.otus.vsh.knb.msCore.message.Message;

import java.util.Optional;

/**
 * Исполняется клиентом [DataBaseMSClient]
 * при обработке сообщения GET_PLAYER_BY_LOGIN
 */
@AllArgsConstructor
public class GetPlayerByLoginDataHandler implements SimpleReceiveRequestHandler<GetPlayerByLoginData, GetPlayerByLoginReplyData> {
    private final GameProcessor gameProcessor;

    @Override
    public Optional<Message<GetPlayerByLoginReplyData>> handle(Message<GetPlayerByLoginData> msg) {
        val login = msg.getBody().getLogin();
        val player = gameProcessor.playerByLogin(login);
        return Optional.of(Message.buildReplyMessage(msg, new GetPlayerByLoginReplyData(player)));
    }
}
