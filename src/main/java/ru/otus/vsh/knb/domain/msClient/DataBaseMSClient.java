package ru.otus.vsh.knb.domain.msClient;

import ru.otus.vsh.knb.msCore.services.HandlersStore;
import ru.otus.vsh.knb.msCore.MessageSystem;
import ru.otus.vsh.knb.msCore.client.CallbackRegistry;
import ru.otus.vsh.knb.msCore.client.MsClient;
import ru.otus.vsh.knb.msCore.client.MsClientImpl;
import ru.otus.vsh.knb.msCore.MsClientNames;

public class DataBaseMSClient extends MsClientImpl implements MsClient {
    public DataBaseMSClient(MessageSystem messageSystem, HandlersStore handlersStore, CallbackRegistry callbackRegistry){
        super(MsClientNames.DATA_BASE.name(), messageSystem, handlersStore, callbackRegistry);
    }
}
