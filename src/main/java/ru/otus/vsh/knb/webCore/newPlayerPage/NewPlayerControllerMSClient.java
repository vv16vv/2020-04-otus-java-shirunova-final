package ru.otus.vsh.knb.webCore.newPlayerPage;

import ru.otus.vsh.knb.msCore.services.HandlersStore;
import ru.otus.vsh.knb.msCore.MessageSystem;
import ru.otus.vsh.knb.msCore.client.CallbackRegistry;
import ru.otus.vsh.knb.msCore.client.MsClient;
import ru.otus.vsh.knb.msCore.client.MsClientImpl;
import ru.otus.vsh.knb.msCore.MsClientNames;

public class NewPlayerControllerMSClient extends MsClientImpl implements MsClient {
    public NewPlayerControllerMSClient(MessageSystem messageSystem, HandlersStore handlersStore, CallbackRegistry callbackRegistry) {
        super(MsClientNames.NEW_PLAYER_CONTROLLER.name(), messageSystem, handlersStore, callbackRegistry);
    }
}
