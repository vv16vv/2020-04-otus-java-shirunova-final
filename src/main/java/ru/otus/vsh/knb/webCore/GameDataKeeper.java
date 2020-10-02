package ru.otus.vsh.knb.webCore;

import ru.otus.vsh.knb.domain.msClient.data.GameData;
import ru.otus.vsh.knb.webCore.gamePage.data.TurnData;

import java.util.Optional;
import java.util.Set;

public interface GameDataKeeper {
    void save(String sessionId, GameData gameData);

    Optional<GameData> get(String sessionId);

    Optional<GameData> get(long gameId);

    Set<String> byGameId(long gameId);

    TurnData getTurnData(long gameId);

}
