package ru.otus.vsh.knb.webCore.newPlayerPage;

import ru.otus.vsh.knb.msCore.MessageSystem;
import ru.otus.vsh.knb.msCore.client.CallbackRegistry;
import ru.otus.vsh.knb.msCore.client.MsClient;

public interface NewPlayerMSClientInitializer {
    MsClient newPlayerControllerMSClient(MessageSystem messageSystem, CallbackRegistry callbackRegistry);
}
