package top.onos.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Liu on 2016/11/23.
 */
@ServerEndpoint("/chatOnline")
@Component
public class ChatSocket{

    private static final Logger logger = LoggerFactory.getLogger(ChatSocket.class);

    private static int onlineCount = 0;

    private static CopyOnWriteArrayList<ChatSocket> chatSockets = new CopyOnWriteArrayList<ChatSocket>();

    private Session session;


    @OnOpen
    public void onOpen(Session session) {
        logger.info("A session opened");
        this.session = session;
        chatSockets.add(this);
        addOnlineCount();
        String id = session.getId();
        for (ChatSocket chatSocket : chatSockets) {
            try {
                chatSocket.sendMessage("欢迎第"+id+"位游客加入聊天室\n");
            } catch (Exception e) {
                continue;
            }
        }
        logger.info(id + "  online");
    }

    @OnClose
    public void onClose(Session session) {
        logger.info("A session closed");
        chatSockets.remove(this);
        subOnlineCount();
        String id = session.getId();
        logger.info(id + "  offline");
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("A message received");
        logger.info(message);
        logger.info(session.toString());
/*        try {
            session.getBasicRemote().sendText(message);
//            this.session.getBasicRemote().sendText("Send a message");
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");

        String msgTime = simpleDateFormat.format(new Date());

        for (ChatSocket chatSocket : chatSockets) {
            try {
                String id = session.getId();
                chatSocket.sendMessage("第" + id + "位游客:&nbsp;&nbsp;" + msgTime + "\n" + message);
            } catch (Exception e) {
                continue;
            }
        }


    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        logger.info("A error happend");

        error.printStackTrace();
    }


    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        ChatSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        ChatSocket.onlineCount--;
    }



}
