package ru.otus.vsh.knb.webCore;

import lombok.AllArgsConstructor;
import lombok.val;
import ru.otus.vsh.knb.domain.GameException;
import ru.otus.vsh.knb.domain.msClient.data.GameData;
import ru.otus.vsh.knb.webCore.gamePage.data.TurnData;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@AllArgsConstructor
public class GameDataKeeperImpl implements GameDataKeeper {
    private final Map<Long, GameData> gameDataByIds = new ConcurrentHashMap<>();
    private final Map<Long, TurnData> turnDataByGameId = new ConcurrentHashMap<>();
    private final Map<String, Long> gameIdBySessionIds = new ConcurrentHashMap<>();

    @Override
    public synchronized void save(String sessionId, GameData gameData) {
        gameDataByIds.put(gameData.getGame().getId(), gameData);
        gameIdBySessionIds.put(sessionId, gameData.getGame().getId());
    }

    @Override
    public synchronized Optional<GameData> get(String sessionId) {
        val gameId = gameIdBySessionIds.getOrDefault(sessionId, -1L);
        if (gameId < 0) return Optional.empty();
        return Optional.ofNullable(gameDataByIds.getOrDefault(gameId, null));
    }

    @Override
    public Optional<GameData> get(long gameId) {
        return Optional.ofNullable(gameDataByIds.getOrDefault(gameId, null));
    }

    @Override
    public Set<String> byGameId(long gameId) {
        return gameIdBySessionIds.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == gameId)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    @Override
    public synchronized TurnData getTurnData(long gameId) {
        val gameData = gameDataByIds.getOrDefault(gameId, null);
        if (gameData == null) throw new GameException(String.format("Incorrect game id %d", gameId));
        if (!turnDataByGameId.containsKey(gameId)) {
            val newTurnData = (new TurnData())
                    .gameData(gameData)
                    .availCheats(gameData.getGame().getSettings().getNumberOfCheats());
            turnDataByGameId.put(gameId, newTurnData);
        }
        return turnDataByGameId.get(gameId);
    }
}
