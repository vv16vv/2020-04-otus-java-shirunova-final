package ru.otus.vsh.knb.domain.msClient.data;

import lombok.Builder;
import lombok.Value;
import ru.otus.vsh.knb.dbCore.model.Game;
import ru.otus.vsh.knb.dbCore.model.Person;
import ru.otus.vsh.knb.webCore.lobbyPage.AvailGameStyles;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Value
@Builder(buildMethodName = "get")
public class GameData implements Comparable<GameData> {
    Game game;
    Person player1;
    @Builder.Default
    Person player2 = null;
    @Builder.Default
    Set<Person> observers = new HashSet<>();
    @Builder.Default
    long wager = 0L;

    public String title() {
        return String.format("%s %s", player1.getLogin(), game.getSettings().title());
    }

    public AvailGameStyles style(@Nonnull Person person) {
        if (player2 == null && person.canAfford(wager)) return AvailGameStyles.AVAILABLE_TO_PLAY;
        else if (player2 == null && !person.canAfford(wager)) return AvailGameStyles.NOT_AVAILABLE;
        else return AvailGameStyles.AVAILABLE_TO_OBSERVE;
    }

    @Override
    public int compareTo(@Nonnull GameData o) {
        if ((player2 != null) && (o.player2 == null)) return 1;
        if ((player2 == null) && (o.player2 != null)) return -1;
        return Objects.compare(wager, o.wager, Comparator.comparingLong(value -> value));
    }
}
