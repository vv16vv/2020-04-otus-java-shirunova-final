package ru.otus.vsh.knb.webCore.lobbyPage;

import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.otus.vsh.knb.domain.msClient.data.NewPlayerData;
import ru.otus.vsh.knb.domain.msClient.data.NewPlayerReplyData;
import ru.otus.vsh.knb.msCore.MessageSystem;
import ru.otus.vsh.knb.msCore.client.CallbackRegistry;
import ru.otus.vsh.knb.msCore.common.CallbackReceiveRequestHandler;
import ru.otus.vsh.knb.msCore.message.MessageType;
import ru.otus.vsh.knb.msCore.services.HandlersStore;
import ru.otus.vsh.knb.msCore.services.HandlersStoreImpl;

@Component
public class LobbyMSClientInitializerImpl implements LobbyMSClientInitializer {
    @Override
    @Bean
    public LobbyControllerMSClient lobbyControllerMSClient(MessageSystem messageSystem, CallbackRegistry callbackRegistry) {
        HandlersStore store = new HandlersStoreImpl();
        store.addHandler(MessageType.AVAIL_GAMES, new CallbackReceiveRequestHandler<NewPlayerData, NewPlayerReplyData>(callbackRegistry));
        val newPlayerControllerMSClient = new LobbyControllerMSClient(messageSystem, store, callbackRegistry);
        messageSystem.addClient(newPlayerControllerMSClient);

        return newPlayerControllerMSClient;
    }
}