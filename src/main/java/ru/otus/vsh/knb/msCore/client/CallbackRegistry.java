package ru.otus.vsh.knb.msCore.client;

import ru.otus.vsh.knb.msCore.common.MessageData;

public interface CallbackRegistry {
    <T extends MessageData> void put(CallbackId id, MessageCallback<T> callback);

    <T extends MessageData> MessageCallback<T> getAndRemove(CallbackId id);
}
