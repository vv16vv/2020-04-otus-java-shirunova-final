package ru.otus.vsh.knb.msCore.client;

import ru.otus.vsh.knb.msCore.common.MessageData;

import java.util.function.Consumer;

public interface MessageCallback<T extends MessageData> extends Consumer<T> {
}
