package top.onos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
        stringRedisTemplate.opsForValue().set("key1", "ssssss");
        return "chat";
    }

    @RequestMapping("/chatp2p/{uid}")
    public String chatP2P(@PathVariable String uid) {
        redisMessageListenerContainer.addMessageListener(messageListenerAdapter,
                                                         new PatternTopic(uid));
        return "chatP2P";
    }

}
