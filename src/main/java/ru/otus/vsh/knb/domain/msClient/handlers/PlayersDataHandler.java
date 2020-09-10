package ru.otus.vsh.knb.domain.msClient.handlers;

import lombok.AllArgsConstructor;
import lombok.val;
import ru.otus.vsh.knb.domain.msClient.data.PlayersReplyData;
import ru.otus.vsh.knb.domain.GameProcessor;
import ru.otus.vsh.knb.msCore.common.EmptyMessageData;
import ru.otus.vsh.knb.msCore.common.SimpleReceiveRequestHandler;
import ru.otus.vsh.knb.msCore.message.Message;

import java.util.Optional;

/**
 * Исполняется клиентом [DataBaseMSClient]
 * при обработке сообщения PLAYERS
 */
@AllArgsConstructor
public class PlayersDataHandler extends SimpleReceiveRequestHandler<EmptyMessageData, PlayersReplyData> {
    private final GameProcessor gameProcessor;

    @Override
    public Optional<Message<PlayersReplyData>> handle(Message<EmptyMessageData> msg) {
        val players = gameProcessor.players();
        return Optional.of(Message.buildReplyMessage(msg, new PlayersReplyData(players)));
    }
}
