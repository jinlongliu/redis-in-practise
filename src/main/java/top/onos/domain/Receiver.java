package top.onos.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.onos.websocket.ChatSocket;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Liu on 2016/11/23.
 */
public class Receiver {
    private static final Logger logger = LoggerFactory.getLogger(Receiver.class);

    private CountDownLatch latch;

    public Receiver(CountDownLatch latch) {
        this.latch = latch;
    }

    public void receiveMessage(String message) {
        ChatSocket.sendAll(message);
        logger.info("Received:" + message);
        latch.countDown();
    }
}
