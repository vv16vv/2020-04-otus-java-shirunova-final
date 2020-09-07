package ru.otus.vsh.knb.dbCore.msClient;

import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.otus.vsh.knb.dbCore.dbService.DBServicePerson;
import ru.otus.vsh.knb.dbCore.msClient.handlers.*;
import ru.otus.vsh.knb.msCore.services.HandlersStore;
import ru.otus.vsh.knb.msCore.services.HandlersStoreImpl;
import ru.otus.vsh.knb.msCore.MessageSystem;
import ru.otus.vsh.knb.msCore.client.CallbackRegistry;
import ru.otus.vsh.knb.msCore.message.MessageType;

@Component
public class DBMSClientInitializerImpl implements DBMSClientInitializer {
    @Override
    @Bean
    public DataBaseMSClient dataBaseMSClient(MessageSystem messageSystem,
                                             CallbackRegistry callbackRegistry,
                                             DBServicePerson dbServicePlayer) {
        HandlersStore store = new HandlersStoreImpl();
        store.addHandler(MessageType.GET_PLAYER_BY_LOGIN, new GetPlayerByLoginDataHandler(dbServicePlayer));
        store.addHandler(MessageType.NEW_PLAYER, new NewPlayerDataHandler(dbServicePlayer));
        store.addHandler(MessageType.PLAYERS, new PlayersDataHandler(dbServicePlayer));
        val databaseMsClient = new DataBaseMSClient(messageSystem, store, callbackRegistry);
        messageSystem.addClient(databaseMsClient);

        return databaseMsClient;
    }
}
