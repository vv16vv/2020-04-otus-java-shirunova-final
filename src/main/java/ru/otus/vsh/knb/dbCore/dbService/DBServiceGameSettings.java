package ru.otus.vsh.knb.dbCore.dbService;

import ru.otus.vsh.knb.dbCore.dbService.api.DBService;
import ru.otus.vsh.knb.dbCore.model.GameSettings;

import java.util.Optional;

public interface DBServiceGameSettings extends DBService<GameSettings> {
    GameSettings getOrCreateSettingsByParams(int numberOfItems, int numberOfTurns, int numberOfCheats);
}
