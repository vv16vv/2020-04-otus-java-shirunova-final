package ru.otus.vsh.knb.webCore.lobbyPage;

import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.otus.vsh.knb.domain.msClient.data.*;
import ru.otus.vsh.knb.msCore.MessageSystem;
import ru.otus.vsh.knb.msCore.client.CallbackRegistry;
import ru.otus.vsh.knb.msCore.common.CallbackCallRequestHandler;
import ru.otus.vsh.knb.msCore.message.MessageType;
import ru.otus.vsh.knb.msCore.services.HandlersStore;
import ru.otus.vsh.knb.msCore.services.HandlersStoreImpl;

@Component
public class LobbyMSClientInitializerImpl implements LobbyMSClientInitializer {
    @Override
    @Bean
    public LobbyControllerMSClient lobbyControllerMSClient(MessageSystem messageSystem, CallbackRegistry callbackRegistry) {
        HandlersStore store = new HandlersStoreImpl();
        store.addHandler(MessageType.AVAIL_GAMES, new CallbackCallRequestHandler<NewPlayerData, NewPlayerReplyData>(callbackRegistry));
        store.addHandler(MessageType.NEW_GAME, new CallbackCallRequestHandler<NewGameData, OneGameReplyData>(callbackRegistry));
        store.addHandler(MessageType.JOIN_GAME, new CallbackCallRequestHandler<JoinGameData, OneGameReplyData>(callbackRegistry));
        val newPlayerControllerMSClient = new LobbyControllerMSClient(messageSystem, store, callbackRegistry);
        messageSystem.addClient(newPlayerControllerMSClient);

        return newPlayerControllerMSClient;
    }
}
