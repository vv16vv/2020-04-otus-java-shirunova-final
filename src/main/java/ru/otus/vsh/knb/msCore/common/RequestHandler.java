package ru.otus.vsh.knb.msCore.common;


import ru.otus.vsh.knb.msCore.message.Message;

import java.util.Optional;

public interface RequestHandler<T extends MessageData, R extends MessageData> {
    Optional<Message<R>> handle(Message<T> msg);
}
