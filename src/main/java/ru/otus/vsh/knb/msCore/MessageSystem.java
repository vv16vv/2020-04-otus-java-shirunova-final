package ru.otus.vsh.knb.msCore;

import ru.otus.vsh.knb.msCore.client.MsClient;
import ru.otus.vsh.knb.msCore.common.MessageData;
import ru.otus.vsh.knb.msCore.message.Message;

public interface MessageSystem {

    void addClient(MsClient msClient);

    void removeClient(String clientId);

    boolean newMessage(Message<? extends MessageData> msg);

    void dispose() throws InterruptedException;

    void dispose(Runnable callback) throws InterruptedException;

    void start();

    int currentQueueSize();
}

