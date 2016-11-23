package top.onos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Liu on 2016/11/22.
 */
@Controller
public class IndexController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisMessageListenerContainer redisMessageListenerContainer;

    @Autowired
    private MessageListenerAdapter messageListenerAdapter;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/chatroom")
    public String chatRoom() {
        stringRedisTemplate.opsForValue().set("keyTest", "valueTest");
        return "chat";
    }

    @RequestMapping("/chatp2p/{uid}")
    public String chatP2P(@PathVariable String uid) {
        redisMessageListenerContainer.addMessageListener(messageListenerAdapter,
                                                         new ChannelTopic(uid));
        return "chatP2P";
    }

    @RequestMapping("/admin")
    public String admin() {
        /*群发页面*/
        return "admin";
    }

    @RequestMapping(value = "/sendAll", method = RequestMethod.POST)
    @ResponseBody
    public String sendAll(String message) {
        /*接收请求，处理操作Redis向相关主题发送消息*/
        stringRedisTemplate.convertAndSend("chat-*", message);
        return "SUCCESS";
    }

}
