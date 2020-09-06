package ru.otus.vsh.knb.dbCore.dbService;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Repository;
import ru.otus.vsh.knb.dbCore.dao.GameSettingsDao;
import ru.otus.vsh.knb.dbCore.dbService.api.AbstractDbServiceImpl;
import ru.otus.vsh.knb.dbCore.model.GameSettings;

import java.util.HashMap;

@Slf4j
@Repository
public class DbServiceGameSettingsImpl extends AbstractDbServiceImpl<GameSettings> implements DBServiceGameSettings {
    private static final String ITEMS = "items";
    private static final String TURNS = "turns";
    private static final String CHEATS = "cheats";
    private final GameSettingsDao gameSettingsDao;

    public DbServiceGameSettingsImpl(GameSettingsDao gameSettingsDao) {
        super(gameSettingsDao);
        this.gameSettingsDao = gameSettingsDao;
    }

    @Override
    public GameSettings getOrCreateSettingsByParams(int numberOfItems, int numberOfTurns, int numberOfCheats) {
        val params = new HashMap<String, Integer>(3);
        params.put(ITEMS, numberOfItems);
        params.put(TURNS, numberOfTurns);
        params.put(CHEATS, numberOfCheats);
        return executeInSession((sm, settings) -> {
            val gameSettingsFromBase = gameSettingsDao.getSettingsByParams(
                    settings.get(ITEMS),
                    settings.get(TURNS),
                    settings.get(CHEATS));
            log.info("found game settings: {}", gameSettingsFromBase);
            GameSettings result;
            if (gameSettingsFromBase.isEmpty()) {
                val newSettings = GameSettings
                        .builder()
                        .numberOfItems(settings.get(ITEMS))
                        .numberOfTurns(settings.get(TURNS))
                        .numberOfCheats(settings.get(CHEATS))
                        .id(0)
                        .get();
                gameSettingsDao.insert(newSettings);
                result = newSettings;
                log.info("no game settings found. Let's create new one: {}", result);
            } else result = gameSettingsFromBase.get();
            sm.commitSession();

            return result;
        }, params);
    }
}
