package ru.otus.vsh.knb.webCore.newPlayerPage;

import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.otus.vsh.knb.domain.msClient.data.NewPlayerData;
import ru.otus.vsh.knb.domain.msClient.data.NewPlayerReplyData;
import ru.otus.vsh.knb.msCore.services.HandlersStore;
import ru.otus.vsh.knb.msCore.services.HandlersStoreImpl;
import ru.otus.vsh.knb.msCore.MessageSystem;
import ru.otus.vsh.knb.msCore.client.CallbackRegistry;
import ru.otus.vsh.knb.msCore.common.CallbackReceiveRequestHandler;
import ru.otus.vsh.knb.msCore.message.MessageType;

@Component
public class NewPlayerMSClientInitializerImpl implements NewPlayerMSClientInitializer {
    @Override
    @Bean
    public NewPlayerControllerMSClient newPlayerControllerMSClient(MessageSystem messageSystem, CallbackRegistry callbackRegistry) {
        HandlersStore store = new HandlersStoreImpl();
        store.addHandler(MessageType.NEW_PLAYER, new CallbackReceiveRequestHandler<NewPlayerData, NewPlayerReplyData>(callbackRegistry));
        val newPlayerControllerMSClient = new NewPlayerControllerMSClient(messageSystem, store, callbackRegistry);
        messageSystem.addClient(newPlayerControllerMSClient);

        return newPlayerControllerMSClient;
    }
}
