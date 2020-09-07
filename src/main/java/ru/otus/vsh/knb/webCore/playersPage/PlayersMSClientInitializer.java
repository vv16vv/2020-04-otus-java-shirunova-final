package ru.otus.vsh.knb.webCore.playersPage;

import ru.otus.vsh.knb.msCore.MessageSystem;
import ru.otus.vsh.knb.msCore.client.CallbackRegistry;
import ru.otus.vsh.knb.msCore.client.MsClient;

public interface PlayersMSClientInitializer {
    MsClient playersControllerMSClient(MessageSystem messageSystem, CallbackRegistry callbackRegistry);
}
