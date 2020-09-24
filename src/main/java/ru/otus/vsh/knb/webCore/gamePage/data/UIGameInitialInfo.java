package ru.otus.vsh.knb.webCore.gamePage.data;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(buildMethodName = "get")
public class UIGameInitialInfo {
    long gameId;

    String playerName1;
    @Builder.Default
    String isPlayer = "true";
    long money1;
    @Builder.Default
    int count1 = 0;

    @Builder.Default
    String playerName2 = "";
    @Builder.Default
    long money2 = 0;
    @Builder.Default
    int count2 = 0;

    int turns;
    List<UIFigure> figures;

    int cheats;

    @Builder.Default
    long bet = 0;
}
