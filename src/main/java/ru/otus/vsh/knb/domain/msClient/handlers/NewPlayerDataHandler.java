package ru.otus.vsh.knb.domain.msClient.handlers;

import lombok.AllArgsConstructor;
import lombok.val;
import ru.otus.vsh.knb.domain.GameProcessor;
import ru.otus.vsh.knb.domain.msClient.data.NewPlayerData;
import ru.otus.vsh.knb.domain.msClient.data.NewPlayerReplyData;
import ru.otus.vsh.knb.msCore.common.SimpleReceiveRequestHandler;
import ru.otus.vsh.knb.msCore.message.Message;

import java.util.Optional;

/**
 * Исполняется клиентом [DataBaseMSClient]
 * при обработке сообщения NEW_PLAYER
 */
@AllArgsConstructor
public class NewPlayerDataHandler implements SimpleReceiveRequestHandler<NewPlayerData, NewPlayerReplyData> {
    private final GameProcessor gameProcessor;

    @Override
    public Optional<Message<NewPlayerReplyData>> handle(Message<NewPlayerData> msg) {
        val newPlayer = gameProcessor.addNewPlayer(
                msg.getBody().getLogin(),
                msg.getBody().getName(),
                msg.getBody().getPassword());
        val result = newPlayer.getId() > 0;
        return Optional.of(Message.buildReplyMessage(msg, new NewPlayerReplyData(result)));
    }
}
