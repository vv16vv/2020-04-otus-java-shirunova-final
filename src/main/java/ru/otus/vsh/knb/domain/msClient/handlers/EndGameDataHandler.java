package ru.otus.vsh.knb.domain.msClient.handlers;

import lombok.AllArgsConstructor;
import ru.otus.vsh.knb.domain.GameProcessor;
import ru.otus.vsh.knb.domain.msClient.data.EndGameData;
import ru.otus.vsh.knb.msCore.common.EmptyMessageData;
import ru.otus.vsh.knb.msCore.common.ResponseProduceRequestHandler;
import ru.otus.vsh.knb.msCore.message.Message;

import java.util.Optional;

/**
 * Исполняется клиентом [DataBaseMSClient]
 * при обработке сообщения END_GAME
 */
@AllArgsConstructor
public class EndGameDataHandler implements ResponseProduceRequestHandler<EndGameData, EmptyMessageData> {
    private final GameProcessor gameProcessor;

    @Override
    public Optional<Message<EmptyMessageData>> handle(Message<EndGameData> msg) {
        gameProcessor.endGame(msg.getBody().getGame());
        return Optional.of(Message.buildReplyMessage(msg, new EmptyMessageData()));
    }
}
