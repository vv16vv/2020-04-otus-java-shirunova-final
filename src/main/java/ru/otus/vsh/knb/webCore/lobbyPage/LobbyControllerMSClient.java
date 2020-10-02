package ru.otus.vsh.knb.webCore.lobbyPage;

import ru.otus.vsh.knb.msCore.MessageSystem;
import ru.otus.vsh.knb.msCore.MsClientNames;
import ru.otus.vsh.knb.msCore.client.CallbackRegistry;
import ru.otus.vsh.knb.msCore.client.MsClient;
import ru.otus.vsh.knb.msCore.client.MsClientImpl;
import ru.otus.vsh.knb.msCore.services.HandlersStore;

public class LobbyControllerMSClient extends MsClientImpl implements MsClient {
    public LobbyControllerMSClient(MessageSystem messageSystem, HandlersStore handlersStore, CallbackRegistry callbackRegistry) {
        super(MsClientNames.LOBBY_CONTROLLER.name(), messageSystem, handlersStore, callbackRegistry);
    }
}
