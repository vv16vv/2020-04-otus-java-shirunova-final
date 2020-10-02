package ru.otus.vsh.knb.domain.msClient.handlers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.otus.vsh.knb.domain.GameProcessor;
import ru.otus.vsh.knb.domain.msClient.data.UpdatePersonData;
import ru.otus.vsh.knb.msCore.common.ResponseProduceRequestHandler;
import ru.otus.vsh.knb.msCore.message.Message;

import java.util.Optional;

/**
 * Исполняется клиентом [DataBaseMSClient]
 * при обработке сообщения UPDATE_PERSON
 */
@AllArgsConstructor
@Slf4j
public class UpdatePersonDataHandler implements ResponseProduceRequestHandler<UpdatePersonData, UpdatePersonData> {
    private final GameProcessor gameProcessor;

    @Override
    public Optional<Message<UpdatePersonData>> handle(Message<UpdatePersonData> msg) {
        gameProcessor.updatePlayerAccount(
                msg.getBody().getPerson()
        );
        return Optional.of(Message.buildReplyMessage(msg, new UpdatePersonData(msg.getBody().getPerson())));
    }
}
