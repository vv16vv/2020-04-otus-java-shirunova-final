package ru.otus.vsh.knb.webCore.gamePage.data;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.otus.vsh.knb.dbCore.model.EventResults;
import ru.otus.vsh.knb.domain.Figure;
import ru.otus.vsh.knb.domain.msClient.data.GameData;

import java.util.concurrent.CyclicBarrier;

@Data
@Accessors(fluent = true, chain = true)
public class TurnData {
    GameData gameData;

    int currentTurn = 0;
    int availCheats = 0;

    Figure figure1 = null;
    Figure figure2 = null;

    int score1 = 0;
    int score2 = 0;

    volatile boolean isProcessing = false;
    CyclicBarrier barrier = new CyclicBarrier(2);

    public synchronized void nextTurn() {
        currentTurn++;
    }

    public EventResults result() {
        if (figure1 == null || figure2 == null) return EventResults.Unknown;
        if (gameData.getRule().isWin(figure1, figure2)) return EventResults.Player1Won;
        if (gameData.getRule().isLost(figure1, figure2)) return EventResults.Player2Won;
        return EventResults.Draw;
    }

    public void increaseScore1(int score) {
        score1 += score;
    }

    public void increaseScore2(int score) {
        score2 += score;
    }
}
