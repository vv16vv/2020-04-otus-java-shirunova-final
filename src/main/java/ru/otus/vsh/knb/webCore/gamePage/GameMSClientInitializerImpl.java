package ru.otus.vsh.knb.webCore.gamePage;

import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.otus.vsh.knb.domain.msClient.data.EndGameData;
import ru.otus.vsh.knb.domain.msClient.data.UpdatePersonData;
import ru.otus.vsh.knb.msCore.MessageSystem;
import ru.otus.vsh.knb.msCore.client.CallbackRegistry;
import ru.otus.vsh.knb.msCore.common.CallbackCallRequestHandler;
import ru.otus.vsh.knb.msCore.common.EmptyMessageData;
import ru.otus.vsh.knb.msCore.message.MessageType;
import ru.otus.vsh.knb.msCore.services.HandlersStore;
import ru.otus.vsh.knb.msCore.services.HandlersStoreImpl;

@Component
public class GameMSClientInitializerImpl implements GameMSClientInitializer {
    @Override
    @Bean
    public GameControllerMSClient gameControllerMSClient(MessageSystem messageSystem, CallbackRegistry callbackRegistry) {
        HandlersStore store = new HandlersStoreImpl();
        store.addHandler(MessageType.END_GAME, new CallbackCallRequestHandler<EndGameData, EmptyMessageData>(callbackRegistry));
        store.addHandler(MessageType.UPDATE_PERSON, new CallbackCallRequestHandler<UpdatePersonData, UpdatePersonData>(callbackRegistry));
        val gameControllerMSClient = new GameControllerMSClient(messageSystem, store, callbackRegistry);
        messageSystem.addClient(gameControllerMSClient);

        return gameControllerMSClient;
    }
}
