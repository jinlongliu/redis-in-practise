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

    /*保存系统所有Sockets,一个Socket即一个Session会话*/
    private static CopyOnWriteArrayList<ChatSocket> chatSockets = new CopyOnWriteArrayList<ChatSocket>();

    private Session session;


    @OnOpen
    public void onOpen(Session session) {
        logger.info("A session opened");
        this.session = session;
        /*游客加入保存会话同时计数+1*/
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
        /*游客离开移除会话，计数减一*/
        chatSockets.remove(this);
        subOnlineCount();
        String id = session.getId();
        logger.info(id + "  offline");
        for (ChatSocket chatSocket : chatSockets) {
            try {
                chatSocket.sendMessage("第"+id+"位游客离开聊天室\n");
            } catch (Exception e) {
                continue;
            }
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("A message received");
        logger.info(message);
        logger.info(session.toString());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        String msgTime = simpleDateFormat.format(new Date());

        String toId = "";
        if (message != null && !message.trim().isEmpty()) {
            /*分析发送消息是群聊还是单聊
            * 如果消息以    {数字}/{消息内容}   格式组合，以/分隔，则是单聊
            * {数字}为session id
            * */
            int num = message.split("/").length;
            if (num > 1) {
                /*单聊*/
                toId = message.split("/")[0];
                if (toId != null && !toId.trim().isEmpty()) {
                    /*对所有会话遍历查找目标会话发送消息*/
                    for (ChatSocket chatSocket : chatSockets) {
                        try {
                            if (chatSocket.session.getId().equals(toId)) {
                                chatSocket.sendMessage("第" + session.getId() + "位游客:&nbsp;&nbsp;" + msgTime + "\n" + message);
                            }
                        } catch (Exception e) {
                            continue;
                        }
                    }

                }
            } else {
                /*群聊：遍历所有会话发送消息*/
                for (ChatSocket chatSocket : chatSockets) {
                    try {
                        String id = session.getId();
                        chatSocket.sendMessage("第" + id + "位游客:&nbsp;&nbsp;" + msgTime + "\n" + message);
                    } catch (Exception e) {
                        continue;
                    }
                }
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


    /*获取在线人数，同时操作在线人数*/
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
