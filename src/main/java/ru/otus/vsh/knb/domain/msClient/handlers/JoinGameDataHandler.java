package ru.otus.vsh.knb.domain.msClient.handlers;

import lombok.AllArgsConstructor;
import lombok.val;
import ru.otus.vsh.knb.domain.GameException;
import ru.otus.vsh.knb.domain.GameProcessor;
import ru.otus.vsh.knb.domain.msClient.data.GameData;
import ru.otus.vsh.knb.domain.msClient.data.JoinGameData;
import ru.otus.vsh.knb.domain.msClient.data.OneGameReplyData;
import ru.otus.vsh.knb.msCore.common.ResponseProduceRequestHandler;
import ru.otus.vsh.knb.msCore.message.Message;

import java.util.Optional;

/**
 * Исполняется клиентом [DataBaseMSClient]
 * при обработке сообщения JOIN_GAME
 */
@AllArgsConstructor
public class JoinGameDataHandler implements ResponseProduceRequestHandler<JoinGameData, OneGameReplyData> {
    private final GameProcessor gameProcessor;

    @Override
    public Optional<Message<OneGameReplyData>> handle(Message<JoinGameData> msg) {
        val game = gameProcessor.gameById(msg.getBody().getGameId()).orElseThrow(() -> new GameException("Game id without an object"));
        GameData gameData;
        if (msg.getBody().isPlayer()) {
            gameData = gameProcessor.joinGameAsPlayer(game, msg.getBody().getPerson());
        } else {
            gameData = gameProcessor.joinGameAsObserver(game, msg.getBody().getPerson());
        }
        return Optional.of(Message.buildReplyMessage(msg, new OneGameReplyData(gameData)));
    }
}
