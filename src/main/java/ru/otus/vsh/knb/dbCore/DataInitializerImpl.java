package ru.otus.vsh.knb.dbCore;

import lombok.val;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.otus.vsh.knb.dbCore.model.Account;
import ru.otus.vsh.knb.dbCore.model.GameSettings;
import ru.otus.vsh.knb.domain.GameProcessor;

@Component
public class DataInitializerImpl implements DataInitializer {
    private final GameProcessor gameProcessor;
    private final PasswordEncoder passwordEncoder;

    public DataInitializerImpl(PasswordEncoder passwordEncoder,
                               GameProcessor gameProcessor
    ) {
        this.gameProcessor = gameProcessor;
        this.passwordEncoder = passwordEncoder;
        createInitialData();
    }

    private Account createDefaultAccount() {
        return Account.builder()
                .id(0L)
                .sum(500L)
                .get();
    }

    @Override
    public void createInitialData() {
        val player1 = gameProcessor.addNewPlayer("vitkus", "Виктория", passwordEncoder.encode("12345"));
        val player2 = gameProcessor.addNewPlayer("sevantius", "Всеволод", passwordEncoder.encode("11111"));
        val player3 = gameProcessor.addNewPlayer("koshir", "Константин", passwordEncoder.encode("24680"));
        val player4 = gameProcessor.addNewPlayer("lanaelle", "Елена", passwordEncoder.encode("99899"), 1000L);
        val player5 = gameProcessor.addNewPlayer("krosider", "Евгений", passwordEncoder.encode("75775"), 1000L);
        val player6 = gameProcessor.addNewPlayer("barabashka", "Ольга", passwordEncoder.encode("1"));
        val player7 = gameProcessor.addNewPlayer("retsam", "Петр", passwordEncoder.encode("1"));

        // default game, without second player, without bet
        // participants: player5 as a first player
        val gameWithoutSndPlayer = gameProcessor.startNewGame(player5);

        // custom game, with second player, with affordable bet
        // participants: player2 as a first player
        // participants: player3 as a second player
        val settings = GameSettings
                .builder()
                .numberOfTurns(10)
                .numberOfCheats(1)
                .get();
        val gameFull = gameProcessor.startNewGame(player2, settings, 100);
        gameProcessor.joinGameAsPlayer(gameFull.orElseThrow(), player3);

        // default game for the player already participating in another game, without second player, with too big bet
        // participants: player4 as a first player
        val gameTooRich = gameProcessor.startNewGame(player4, settings, 900);

    }
}
