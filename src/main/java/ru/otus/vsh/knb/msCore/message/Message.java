package ru.otus.vsh.knb.msCore.message;

import lombok.*;
import ru.otus.vsh.knb.msCore.client.CallbackId;
import ru.otus.vsh.knb.msCore.common.EmptyMessageData;
import ru.otus.vsh.knb.msCore.common.MessageData;

import javax.annotation.Nonnull;
import java.io.Serializable;

@Value
@Builder(buildMethodName = "get", builderClassName = "Builder", toBuilder = true)
public class Message<T extends MessageData> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Getter
    private static final Message<EmptyMessageData> VOID_MESSAGE =
            new Builder<EmptyMessageData>()
                    .from("")
                    .to("")
                    .type(MessageType.VOID)
                    .body(new EmptyMessageData())
                    .get();

    String from;
    String to;

    MessageType type;

    @lombok.Builder.Default
    MessageId id = new MessageId();
    MessageId sourceMessageId;

    T body;

    @With
    CallbackId callbackId;

    @NonNull
    public static <T extends MessageData, R extends MessageData> Message<R> buildReplyMessage(@Nonnull Message<T> message, @Nonnull R data) {
        return buildReplyMessage(message, message.type, data);
    }

    @NonNull
    public static <T extends MessageData, R extends MessageData> Message<R> buildReplyMessage(@Nonnull Message<T> message, @Nonnull MessageType newType, @Nonnull R data) {
        return new Builder<R>()
                .from(message.to)
                .to(message.from)
                .type(newType)
                .sourceMessageId(message.id)
                .body(data)
                .callbackId(message.callbackId)
                .get();
    }

    @NonNull
    public static <T extends MessageData, R extends MessageData> Message<T> produceMessage(@Nonnull String from,
                                                                                           @Nonnull String to,
                                                                                           @Nonnull MessageType type,
                                                                                           @Nonnull T data,
                                                                                           @Nonnull CallbackId callbackId) {
        return new Builder<T>()
                .from(from)
                .to(to)
                .type(type)
                .body(data)
                .callbackId(callbackId)
                .get();
    }
}
