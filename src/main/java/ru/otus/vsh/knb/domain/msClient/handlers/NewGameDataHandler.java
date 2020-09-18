package ru.otus.vsh.knb.domain.msClient.handlers;

import lombok.AllArgsConstructor;
import lombok.val;
import ru.otus.vsh.knb.domain.GameException;
import ru.otus.vsh.knb.domain.GameProcessor;
import ru.otus.vsh.knb.domain.msClient.data.GameData;
import ru.otus.vsh.knb.domain.msClient.data.NewGameData;
import ru.otus.vsh.knb.domain.msClient.data.NewGameReplyData;
import ru.otus.vsh.knb.msCore.common.ResponseProduceRequestHandler;
import ru.otus.vsh.knb.msCore.message.Message;

import java.util.Optional;

/**
 * Исполняется клиентом [DataBaseMSClient]
 * при обработке сообщения NEW_PLAYER
 */
@AllArgsConstructor
public class NewGameDataHandler implements ResponseProduceRequestHandler<NewGameData, NewGameReplyData> {
    private final GameProcessor gameProcessor;

    @Override
    public Optional<Message<NewGameReplyData>> handle(Message<NewGameData> msg) {
        val settings = gameProcessor.getSettings(
                msg.getBody().getItems(),
                msg.getBody().getTurns(),
                msg.getBody().getCheats()
        );
        val newGame = gameProcessor.startNewGame(
                msg.getBody().getPlayer1(),
                settings,
                msg.getBody().getWager())
                .orElseThrow(() -> new GameException("Cannot create new game"));
        val gameData = GameData.builder()
                .game(newGame)
                .player1(msg.getBody().getPlayer1())
                .wager(msg.getBody().getWager())
                .get();
        return Optional.of(Message.buildReplyMessage(msg, new NewGameReplyData(gameData)));
    }
}
