package ru.otus.vsh.knb.dbCore.msClient;

import ru.otus.vsh.knb.dbCore.dbService.DBServicePerson;
import ru.otus.vsh.knb.msCore.MessageSystem;
import ru.otus.vsh.knb.msCore.client.CallbackRegistry;
import ru.otus.vsh.knb.msCore.client.MsClient;

public interface DBMSClientInitializer {
    MsClient dataBaseMSClient(MessageSystem messageSystem,
                              CallbackRegistry callbackRegistry,
                              DBServicePerson dbServicePlayer);
}
