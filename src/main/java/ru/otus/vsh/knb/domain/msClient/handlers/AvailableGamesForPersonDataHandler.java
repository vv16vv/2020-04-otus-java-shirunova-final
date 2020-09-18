package ru.otus.vsh.knb.domain.msClient.handlers;

import lombok.AllArgsConstructor;
import lombok.val;
import ru.otus.vsh.knb.domain.GameProcessor;
import ru.otus.vsh.knb.domain.msClient.data.AvailableGamesForPersonData;
import ru.otus.vsh.knb.domain.msClient.data.AvailableGamesForPersonReplayData;
import ru.otus.vsh.knb.msCore.common.ResponseProduceRequestHandler;
import ru.otus.vsh.knb.msCore.message.Message;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Исполняется клиентом [DataBaseMSClient]
 * при обработке сообщения AVAIL_GAMES
 */
@AllArgsConstructor
public class AvailableGamesForPersonDataHandler implements ResponseProduceRequestHandler<AvailableGamesForPersonData, AvailableGamesForPersonReplayData> {
    private final GameProcessor gameProcessor;

    @Override
    public Optional<Message<AvailableGamesForPersonReplayData>> handle(Message<AvailableGamesForPersonData> msg) {
        val person = gameProcessor.playerByLogin(msg.getBody().getPersonLogin()).orElseThrow();
        val games = new ArrayList<>(gameProcessor.gamesToJoinAsObserver(person));
        return Optional.of(Message.buildReplyMessage(msg, new AvailableGamesForPersonReplayData(person, games)));
    }
}
