package ru.otus.vsh.knb.webCore;

import lombok.val;
import ru.otus.vsh.knb.domain.msClient.data.GameData;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class GameDataKeeperImpl implements GameDataKeeper {
    private final Map<String, GameData> repository = new ConcurrentHashMap<>();

    @Override
    public void add(String sessionId, GameData gameData) {
        repository.put(sessionId, gameData);
    }

    @Override
    public Optional<GameData> get(String sessionId) {
        return Optional.ofNullable(repository.getOrDefault(sessionId, null));
    }

    @Override
    public synchronized void addAndUpdate(String sessionId, GameData gameData) {
        for (val entry : repository.entrySet()) {
            if (entry.getValue().getGame().getId() == gameData.getGame().getId()) {
                repository.put(entry.getKey(), gameData);
            }
        }
        add(sessionId, gameData);
    }
}
