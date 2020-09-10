package ru.otus.vsh.knb.domain.msClient;

import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.otus.vsh.knb.domain.msClient.handlers.AvailableGamesForPersonDataHandler;
import ru.otus.vsh.knb.domain.msClient.handlers.GetPlayerByLoginDataHandler;
import ru.otus.vsh.knb.domain.msClient.handlers.NewPlayerDataHandler;
import ru.otus.vsh.knb.domain.msClient.handlers.PlayersDataHandler;
import ru.otus.vsh.knb.domain.GameProcessor;
import ru.otus.vsh.knb.msCore.MessageSystem;
import ru.otus.vsh.knb.msCore.client.CallbackRegistry;
import ru.otus.vsh.knb.msCore.message.MessageType;
import ru.otus.vsh.knb.msCore.services.HandlersStore;
import ru.otus.vsh.knb.msCore.services.HandlersStoreImpl;

@Component
public class DBMSClientInitializerImpl implements DBMSClientInitializer {
    @Override
    @Bean
    public DataBaseMSClient dataBaseMSClient(MessageSystem messageSystem,
                                             CallbackRegistry callbackRegistry,
                                             GameProcessor gameProcessor) {
        HandlersStore store = new HandlersStoreImpl();
        store.addHandler(MessageType.GET_PLAYER_BY_LOGIN, new GetPlayerByLoginDataHandler(gameProcessor));
        store.addHandler(MessageType.NEW_PLAYER, new NewPlayerDataHandler(gameProcessor));
        store.addHandler(MessageType.PLAYERS, new PlayersDataHandler(gameProcessor));
        store.addHandler(MessageType.AVAIL_GAMES, new AvailableGamesForPersonDataHandler(gameProcessor));
        val databaseMsClient = new DataBaseMSClient(messageSystem, store, callbackRegistry);
        messageSystem.addClient(databaseMsClient);

        return databaseMsClient;
    }
}
