package ru.otus.vsh.knb.domain.msClient.data;

import lombok.Builder;
import lombok.Value;
import ru.otus.vsh.knb.dbCore.model.Game;
import ru.otus.vsh.knb.dbCore.model.Person;
import ru.otus.vsh.knb.dbCore.model.Roles;
import ru.otus.vsh.knb.domain.GameException;
import ru.otus.vsh.knb.domain.GameRule;
import ru.otus.vsh.knb.domain.GameRules;
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

    public Roles getPersonRole(@Nonnull Person person) {
        if (person.getId() == player1.getId()) return Roles.Player1;
        if ((player2 != null) && (person.getId() == player2.getId())) return Roles.Player2;
        if (observers
                .stream()
                .anyMatch(observer -> observer.getId() == person.getId())) return Roles.Observer;
        return Roles.NotParticipate;
    }

    public GameRule getRule() {
        return GameRules
                .get(game.getSettings().getNumberOfItems())
                .orElseThrow(() -> new GameException(String.format("Incorrect number of items %d", game.getSettings().getNumberOfItems())));
    }
}
