package ru.otus.vsh.knb.webCore.playersPage;

import ru.otus.vsh.knb.msCore.services.HandlersStore;
import ru.otus.vsh.knb.msCore.MessageSystem;
import ru.otus.vsh.knb.msCore.client.CallbackRegistry;
import ru.otus.vsh.knb.msCore.client.MsClient;
import ru.otus.vsh.knb.msCore.client.MsClientImpl;
import ru.otus.vsh.knb.msCore.MsClientNames;

public class PlayersControllerMSClient extends MsClientImpl implements MsClient {
    public PlayersControllerMSClient(MessageSystem messageSystem, HandlersStore handlersStore, CallbackRegistry callbackRegistry) {
        super(MsClientNames.PLAYERS_CONTROLLER.name(), messageSystem, handlersStore, callbackRegistry);
    }
}
