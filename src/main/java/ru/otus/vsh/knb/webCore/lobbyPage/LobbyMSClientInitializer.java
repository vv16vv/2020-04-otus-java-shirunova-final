package ru.otus.vsh.knb.webCore.lobbyPage;

import ru.otus.vsh.knb.msCore.MessageSystem;
import ru.otus.vsh.knb.msCore.client.CallbackRegistry;
import ru.otus.vsh.knb.msCore.client.MsClient;

public interface LobbyMSClientInitializer {
    MsClient lobbyControllerMSClient(MessageSystem messageSystem, CallbackRegistry callbackRegistry);
}
