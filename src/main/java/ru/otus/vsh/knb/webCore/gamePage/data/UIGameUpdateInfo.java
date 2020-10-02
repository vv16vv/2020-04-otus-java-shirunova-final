package ru.otus.vsh.knb.webCore.gamePage.data;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(buildMethodName = "get")
public class UIGameUpdateInfo {
    String playerName2;
    long money2;
}
