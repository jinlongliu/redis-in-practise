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

    public void receiveMessage(String message, String topic) {
        /*订阅Redis消息，发送给所有WebSocket，实现群发，消息记录在Redis Server*/
        ChatSocket.sendAll(message);
        logger.info("Received:" + message);
        logger.info("Topic:" + topic);
        latch.countDown();
    }
}
