package ru.otus.vsh.knb.webCore.playersPage;

import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.otus.vsh.knb.domain.msClient.data.NewPlayerData;
import ru.otus.vsh.knb.domain.msClient.data.NewPlayerReplyData;
import ru.otus.vsh.knb.msCore.services.HandlersStore;
import ru.otus.vsh.knb.msCore.services.HandlersStoreImpl;
import ru.otus.vsh.knb.msCore.MessageSystem;
import ru.otus.vsh.knb.msCore.client.CallbackRegistry;
import ru.otus.vsh.knb.msCore.common.CallbackCallRequestHandler;
import ru.otus.vsh.knb.msCore.message.MessageType;

@Component
public class PlayersMSClientInitializerImpl implements PlayersMSClientInitializer {
    @Override
    @Bean
    public PlayersControllerMSClient playersControllerMSClient(MessageSystem messageSystem, CallbackRegistry callbackRegistry) {
        HandlersStore store = new HandlersStoreImpl();
        store.addHandler(MessageType.PLAYERS, new CallbackCallRequestHandler<NewPlayerData, NewPlayerReplyData>(callbackRegistry));
        val newPlayerControllerMSClient = new PlayersControllerMSClient(messageSystem, store, callbackRegistry);
        messageSystem.addClient(newPlayerControllerMSClient);

        return newPlayerControllerMSClient;
    }
}
