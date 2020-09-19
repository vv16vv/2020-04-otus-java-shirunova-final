package ru.otus.vsh.knb.webCore;

import ru.otus.vsh.knb.domain.msClient.data.GameData;

import java.util.Optional;

public interface GameDataKeeper {
    void add(String sessionId, GameData gameData);

    Optional<GameData> get(String sessionId);

    void addAndUpdate(String sessionId, GameData gameData);

}
