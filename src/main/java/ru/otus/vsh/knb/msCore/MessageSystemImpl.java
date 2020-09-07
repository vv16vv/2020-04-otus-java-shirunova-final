package ru.otus.vsh.knb.msCore;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.otus.vsh.knb.msCore.client.MsClient;
import ru.otus.vsh.knb.msCore.common.MessageData;
import ru.otus.vsh.knb.msCore.message.Message;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
@Component
public final class MessageSystemImpl implements MessageSystem {
    private static final int MESSAGE_QUEUE_SIZE = 100_000;
    private static final int MSG_HANDLER_THREAD_LIMIT = 2;

    private final AtomicBoolean runFlag = new AtomicBoolean(true);

    private final Map<String, MsClient> clientMap = new ConcurrentHashMap<>();
    private final BlockingQueue<Message<? extends MessageData>> messageQueue = new ArrayBlockingQueue<>(MESSAGE_QUEUE_SIZE);
    private final ExecutorService msgProcessor = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable);
        thread.setName("msg-processor-thread");
        return thread;
    });

    private final ExecutorService msgHandler = Executors.newFixedThreadPool(MSG_HANDLER_THREAD_LIMIT,
            new ThreadFactory() {

                private final AtomicInteger threadNameSeq = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable runnable) {
                    Thread thread = new Thread(runnable);
                    thread.setName("msg-handler-thread-" + threadNameSeq.incrementAndGet());
                    return thread;
                }
            });
    private Runnable disposeCallback;

    public MessageSystemImpl() {
        start();
    }

    public MessageSystemImpl(boolean startProcessing) {
        if (startProcessing) {
            start();
        }
    }

    @Override
    public void start() {
        msgProcessor.submit(this::processMessages);
    }

    @Override
    public int currentQueueSize() {
        return messageQueue.size();
    }

    @Override
    public void addClient(MsClient msClient) {
        log.info("new client:{}", msClient.getName());
        if (clientMap.containsKey(msClient.getName())) {
            throw new IllegalArgumentException("Error. client: " + msClient.getName() + " already exists");
        }
        clientMap.put(msClient.getName(), msClient);
    }

    @Override
    public void removeClient(String clientId) {
        MsClient removedClient = clientMap.remove(clientId);
        if (removedClient == null) {
            log.warn("client not found: {}", clientId);
        } else {
            log.info("removed client:{}", removedClient);
        }
    }

    @Override
    public boolean newMessage(Message<? extends MessageData> msg) {
        if (runFlag.get()) {
            return messageQueue.offer(msg);
        } else {
            log.warn("MS is being shutting down... rejected:{}", msg);
            return false;
        }
    }

    @Override
    public void dispose() throws InterruptedException {
        log.info("now in the messageQueue {} messages", currentQueueSize());
        runFlag.set(false);
        insertStopMessage();
        msgProcessor.shutdown();
        msgHandler.awaitTermination(60, TimeUnit.SECONDS);
    }

    @Override
    public void dispose(Runnable callback) throws InterruptedException {
        disposeCallback = callback;
        dispose();
    }

    private void processMessages() {
        log.info("msgProcessor started, {}", currentQueueSize());
        while (runFlag.get() || !messageQueue.isEmpty()) {
            try {
                Message<? extends MessageData> msg = messageQueue.take();
                if (msg == Message.getVOID_MESSAGE()) {
                    log.info("received the stop message");
                } else {
                    MsClient clientTo = clientMap.get(msg.getTo());
                    if (clientTo == null) {
                        log.warn("client not found");
                    } else {
                        msgHandler.submit(() -> handleMessage(clientTo, msg));
                    }
                }
            } catch (InterruptedException ex) {
                log.error(ex.getMessage(), ex);
                Thread.currentThread().interrupt();
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        }

        if (disposeCallback != null) {
            msgHandler.submit(disposeCallback);
        }
        msgHandler.submit(this::messageHandlerShutdown);
        log.info("msgProcessor finished");
    }

    private void messageHandlerShutdown() {
        msgHandler.shutdown();
        log.info("msgHandler has been shut down");
    }


    private void handleMessage(MsClient msClient, Message<? extends MessageData> msg) {
        try {
            msClient.handle(msg);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            log.error("message:{}", msg);
        }
    }

    private void insertStopMessage() throws InterruptedException {
        boolean result = messageQueue.offer(Message.getVOID_MESSAGE());
        while (!result) {
            Thread.sleep(100);
            result = messageQueue.offer(Message.getVOID_MESSAGE());
        }
    }

}
