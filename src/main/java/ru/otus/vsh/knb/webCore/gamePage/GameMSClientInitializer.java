package ru.otus.vsh.knb.webCore.gamePage;

import ru.otus.vsh.knb.msCore.MessageSystem;
import ru.otus.vsh.knb.msCore.client.CallbackRegistry;
import ru.otus.vsh.knb.msCore.client.MsClient;

public interface GameMSClientInitializer {
    MsClient gameControllerMSClient(MessageSystem messageSystem, CallbackRegistry callbackRegistry);
}
