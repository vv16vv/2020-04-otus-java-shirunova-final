package ru.otus.vsh.knb.dbCore.dao;

import ru.otus.vsh.knb.dbCore.model.GameSettings;

import java.util.Optional;

public interface GameSettingsDao extends Dao<GameSettings> {
    Optional<GameSettings> getSettingsByParams(int numberOfItems, int numberOfTurns, int numberOfCheats);
}
