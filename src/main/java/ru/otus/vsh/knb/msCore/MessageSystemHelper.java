package ru.otus.vsh.knb.msCore;

import lombok.experimental.UtilityClass;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.concurrent.CountDownLatch;

import static java.util.concurrent.TimeUnit.SECONDS;

@UtilityClass
public class MessageSystemHelper {

    public void waitForAnswer(@Nonnull Duration duration, @Nonnull CountDownLatch latch) {
        try {
            if (!latch.await(duration.getSeconds(), SECONDS)) {
                throw new TimeoutException();
            }
        } catch (InterruptedException ignore) {
        }
    }

    public void waitForAnswer(@Nonnull CountDownLatch latch) {
        waitForAnswer(Duration.ofSeconds(30), latch);
    }

}
