package ru.otus.vsh.knb.msCore.services;

import org.springframework.stereotype.Component;
import ru.otus.vsh.knb.msCore.common.MessageData;
import ru.otus.vsh.knb.msCore.common.RequestHandler;
import ru.otus.vsh.knb.msCore.message.MessageType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class HandlersStoreImpl implements HandlersStore {
    private final Map<MessageType, RequestHandler<? extends MessageData, ? extends MessageData>> handlers = new ConcurrentHashMap<>();

    @Override
    public RequestHandler<? extends MessageData, ? extends MessageData> getHandlerByType(MessageType messageType) {
        return handlers.get(messageType);
    }

    @Override
    public void addHandler(MessageType messageType, RequestHandler<? extends MessageData, ? extends MessageData> handler) {
        handlers.put(messageType, handler);
    }

    @Override
    public boolean isEmpty() {
        return handlers.isEmpty();
    }
}
