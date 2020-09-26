package ru.otus.vsh.knb.domain.msClient.handlers;

import lombok.AllArgsConstructor;
import ru.otus.vsh.knb.domain.GameProcessor;
import ru.otus.vsh.knb.domain.msClient.data.UpdatePersonData;
import ru.otus.vsh.knb.msCore.common.EmptyMessageData;
import ru.otus.vsh.knb.msCore.common.ResponseProduceRequestHandler;
import ru.otus.vsh.knb.msCore.message.Message;

import java.util.Optional;

/**
 * Исполняется клиентом [DataBaseMSClient]
 * при обработке сообщения UPDATE_PERSON
 */
@AllArgsConstructor
public class UpdatePersonDataHandler implements ResponseProduceRequestHandler<UpdatePersonData, EmptyMessageData> {
    private final GameProcessor gameProcessor;

    @Override
    public Optional<Message<EmptyMessageData>> handle(Message<UpdatePersonData> msg) {
        gameProcessor.updatePlayerAccount(
                msg.getBody().getPerson()
        );
        return Optional.of(Message.buildReplyMessage(msg, new EmptyMessageData()));
    }
}
