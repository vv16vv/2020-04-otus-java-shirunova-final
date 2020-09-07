package ru.otus.vsh.knb.msCore.client;

import org.springframework.stereotype.Component;
import ru.otus.vsh.knb.msCore.common.MessageData;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CallbackRegistryImpl implements CallbackRegistry {

    private final Map<CallbackId, MessageCallback<? extends MessageData>> callbackRegistry = new ConcurrentHashMap<>();

    @Override
    public <T extends MessageData> void put(CallbackId id, MessageCallback<T> callback) {
        callbackRegistry.put(id, callback);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends MessageData> MessageCallback<T> getAndRemove(CallbackId id) {
        return (MessageCallback<T>) callbackRegistry.remove(id);
    }
}
