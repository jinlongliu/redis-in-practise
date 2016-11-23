package top.onos.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.sockjs.SockJsService;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * Created by Liu on 2016/11/23.
 */
@ServerEndpoint("/chatOnline")
@Component
public class ChatSocket{

    private static final Logger logger = LoggerFactory.getLogger(ChatSocket.class);

    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        logger.info("A session opened");
        this.session = session;

    }

    @OnClose
    public void onClose() {
        logger.info("A session closed");

    }

    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("A message received");
        logger.info(message);
        logger.info(session.toString());
        try {
            session.getBasicRemote().sendText(message);
//            this.session.getBasicRemote().sendText("Send a message");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void onError(Session session, Throwable error) {
        logger.info("A error happend");

        error.printStackTrace();
    }
}
