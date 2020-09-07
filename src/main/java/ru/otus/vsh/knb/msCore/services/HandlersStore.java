package ru.otus.vsh.knb.msCore.services;

import ru.otus.vsh.knb.msCore.common.MessageData;
import ru.otus.vsh.knb.msCore.common.RequestHandler;
import ru.otus.vsh.knb.msCore.message.MessageType;

public interface HandlersStore {
    RequestHandler<? extends MessageData, ? extends MessageData> getHandlerByType(MessageType messageType);

    void addHandler(MessageType messageType, RequestHandler<? extends MessageData, ? extends MessageData> handler);

    boolean isEmpty();
}
