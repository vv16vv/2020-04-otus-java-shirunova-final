package ru.otus.vsh.knb.domain.msClient;

import ru.otus.vsh.knb.domain.GameProcessor;
import ru.otus.vsh.knb.msCore.MessageSystem;
import ru.otus.vsh.knb.msCore.client.CallbackRegistry;
import ru.otus.vsh.knb.msCore.client.MsClient;

public interface DBMSClientInitializer {
    MsClient dataBaseMSClient(MessageSystem messageSystem,
                              CallbackRegistry callbackRegistry,
                              GameProcessor gameProcessor);
}
