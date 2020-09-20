package ru.otus.vsh.knb.webCore.gamePage.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(buildMethodName = "get")
public class UIGameInitialInfo {
    String playerName1;
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
    @Builder.Default
    int currentTurn = 0;

    int cheats;
    int availCheats;

    @Builder.Default
    long bet = 0;
}
