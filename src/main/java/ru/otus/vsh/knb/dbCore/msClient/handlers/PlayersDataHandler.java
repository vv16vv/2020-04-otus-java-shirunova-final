package ru.otus.vsh.knb.dbCore.msClient.handlers;

import lombok.AllArgsConstructor;
import lombok.val;
import ru.otus.vsh.knb.dbCore.dbService.DBServicePerson;
import ru.otus.vsh.knb.dbCore.msClient.data.PlayersReplyData;
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
    private final DBServicePerson dbServicePlayer;

    @Override
    public Optional<Message<PlayersReplyData>> handle(Message<EmptyMessageData> msg) {
        val players = dbServicePlayer.findAll();
        return Optional.of(Message.buildReplyMessage(msg, new PlayersReplyData(players)));
    }
}
