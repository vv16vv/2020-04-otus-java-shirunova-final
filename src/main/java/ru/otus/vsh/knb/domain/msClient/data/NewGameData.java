package ru.otus.vsh.knb.domain.msClient.data;

import lombok.Builder;
import lombok.Value;
import ru.otus.vsh.knb.dbCore.model.Person;
import ru.otus.vsh.knb.domain.DefaultValues;
import ru.otus.vsh.knb.msCore.common.MessageData;

@Value
@Builder(buildMethodName = "get")
public class NewGameData implements MessageData {
    Person player1;
    @Builder.Default
    int turns = DefaultValues.SIMPLE_SETTINGS_TURNS;
    @Builder.Default
    int items = DefaultValues.SIMPLE_SETTINGS_ITEMS;
    @Builder.Default
    int cheats = DefaultValues.SIMPLE_SETTINGS_CHEATS;
    @Builder.Default
    long wager = 0L;
}
