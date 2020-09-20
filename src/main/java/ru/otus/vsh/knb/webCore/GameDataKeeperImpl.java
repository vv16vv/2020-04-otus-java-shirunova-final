package ru.otus.vsh.knb.webCore;

import lombok.val;
import ru.otus.vsh.knb.dbCore.model.Game;
import ru.otus.vsh.knb.domain.msClient.data.GameData;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
        for (val key : byGame(gameData.getGame())) {
            repository.put(key, gameData);
        }
        add(sessionId, gameData);
    }

    @Override
    public Set<String> byGame(Game game) {
        return repository.entrySet()
                .stream()
                .filter(entry -> entry.getValue().getGame().getId() == game.getId())
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }
}
