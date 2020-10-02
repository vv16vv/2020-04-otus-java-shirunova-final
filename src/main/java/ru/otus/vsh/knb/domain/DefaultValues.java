package ru.otus.vsh.knb.domain;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DefaultValues {
    public int SIMPLE_SETTINGS_ITEMS = 3;
    public int SIMPLE_SETTINGS_TURNS = 3;
    public int SIMPLE_SETTINGS_CHEATS = 0;

    public long INITIAL_SUM = 500L;
    public long GOLD_ONE_TURN_WIN = 50L;
    public long GOLD_ONE_TURN_DRAW = 25L;
    public long GOLD_ONE_CHEAT = 100L;
    public long GOLD_ONE_GAME_WIN = 1000L;
    public long GOLD_ONE_GAME_DRAW = 500L;

    public int POINTS_WINNING = 2;
    public int POINTS_DRAW = 1;
    public int POINTS_LOSING = 0;

}
