package ru.otus.vsh.knb.webCore.gamePage.data;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(buildMethodName = "get")
public class UIResultInfo {
    // figure chosen by current player
    UIFigure figure1;
    // figure chosen by another player
    UIFigure figure2;
    // relatively to the current player
    String resultText;

    // for current player
    long money1;
    int count1;

    // for another player
    long money2;
    int count2;
}
