package ru.otus.vsh.knb.webCore;

import ru.otus.vsh.knb.dbCore.model.Game;
import ru.otus.vsh.knb.domain.msClient.data.GameData;

import java.util.Optional;
import java.util.Set;

public interface GameDataKeeper {
    void add(String sessionId, GameData gameData);

    Optional<GameData> get(String sessionId);

    void addAndUpdate(String sessionId, GameData gameData);

    Set<String> byGame(Game game);

}
