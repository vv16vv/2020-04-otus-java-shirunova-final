package ru.otus.vsh.knb.msCore.client;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.otus.vsh.knb.msCore.services.HandlersStore;
import ru.otus.vsh.knb.msCore.MessageSystem;
import ru.otus.vsh.knb.msCore.common.MessageData;
import ru.otus.vsh.knb.msCore.common.RequestHandler;
import ru.otus.vsh.knb.msCore.message.Message;
import ru.otus.vsh.knb.msCore.message.MessageType;

@Slf4j
@EqualsAndHashCode(of = {"name"})
@AllArgsConstructor
public class MsClientImpl implements MsClient {

    private final String name;
    private final MessageSystem messageSystem;
    private final HandlersStore handlersStore;
    private final CallbackRegistry callbackRegistry;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public <T extends MessageData> boolean sendMessage(Message<T> msg) {
        boolean result = messageSystem.newMessage(msg);
        if (!result) {
            log.error("the last message was rejected: {}", msg);
        }
        return result;
    }

    @SuppressWarnings("all")
    @Override
    public void handle(Message msg) {
        log.info("new message:{}", msg);
        try {
            RequestHandler requestHandler = handlersStore.getHandlerByType(msg.getType());
            if (requestHandler != null) {
                requestHandler.handle(msg).ifPresent(message -> sendMessage((Message) message));
            } else {
                log.error("handler not found for the message type:{}", msg.getType());
            }
        } catch (Exception ex) {
            log.error("msg:{}", msg, ex);
        }
    }

    @Override
    public <T extends MessageData, R extends MessageData> Message<T> produceMessage(String to,
                                                          T data,
                                                          MessageType msgType,
                                                          MessageCallback<R> callback) {
        val message = Message.produceMessage(name, to, msgType, data, new CallbackId());
        callbackRegistry.put(message.getCallbackId(), callback);
        return message;
    }

}
