package ru.otus.vsh.knb.webCore.gamePage.data;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import ru.otus.vsh.knb.dbCore.model.EventResults;
import ru.otus.vsh.knb.domain.Figure;
import ru.otus.vsh.knb.domain.msClient.data.GameData;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

@Data
@Accessors(fluent = true, chain = true)
@Slf4j
public class TurnData {
    GameData gameData;

    volatile int currentTurn = 0;
    volatile int availCheats = 0;

    volatile Figure figure1 = null;
    volatile Figure figure2 = null;

    volatile int score1 = 0;
    volatile int score2 = 0;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
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

    public synchronized void increaseScore1(int score) {
        score1 += score;
    }

    public synchronized void increaseScore2(int score) {
        score2 += score;
    }

    public void resetBarrier() {
        barrier.reset();
    }

    public synchronized void figure1(Figure figure) {
        log.warn("set figure1 {}", figure);
        this.figure1 = figure;
    }

    public synchronized void figure2(Figure figure) {
        log.warn("set figure2 {}", figure);
        this.figure2 = figure;
    }

    public void awaitBarrier() {
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

}
