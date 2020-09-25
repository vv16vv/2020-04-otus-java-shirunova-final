package ru.otus.vsh.knb.webCore.gamePage;

import ru.otus.vsh.knb.msCore.MessageSystem;
import ru.otus.vsh.knb.msCore.MsClientNames;
import ru.otus.vsh.knb.msCore.client.CallbackRegistry;
import ru.otus.vsh.knb.msCore.client.MsClient;
import ru.otus.vsh.knb.msCore.client.MsClientImpl;
import ru.otus.vsh.knb.msCore.services.HandlersStore;

public class GameControllerMSClient extends MsClientImpl implements MsClient {
    public GameControllerMSClient(MessageSystem messageSystem, HandlersStore handlersStore, CallbackRegistry callbackRegistry) {
        super(MsClientNames.GAME_CONTROLLER.name(), messageSystem, handlersStore, callbackRegistry);
    }
}
