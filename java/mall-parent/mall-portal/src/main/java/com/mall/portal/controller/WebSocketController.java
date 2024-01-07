package com.mall.portal.controller;
/**
 * @Description: Web Socket Controller
 * - 每一个 WebSocket 连接维持一个 Session 对象，用于服务器向客户端发送信息
 * - 我们可以将 User-Session 装到 ConcurrentHashMap 中，给某一个客户端发送信息或群发消息，此案列使用群发信息
 */

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/api/webSocket")
@Component
public class WebSocketController {

    //    private final static Logger LOGGER = LoggerFactory.getLogger(WebSocketController.class);
    private static CopyOnWriteArraySet<Session> sessions = new CopyOnWriteArraySet<Session>();


    /**
     * - 创建连接
     * - 在此使用 @PathParam 接受 Path 参数
     */

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
//        LOGGER.debug(String.format("新建连接，连接总数 %d。", sessions.size()));
    }


    // 关闭连接
    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
//        LOGGER.debug(String.format("断开连接，连接总数 %d。", sessions.size()));
    }
    // 发生错误
    @OnError
    public void onError(Throwable throwable){
//        LOGGER.debug("发生错误");
        throwable.printStackTrace();

    }

    // 接受客户端消息
    @OnMessage
    public void onMessage(String message) {
//        LOGGER.debug(String.format("收到消息，%s，连接总数 %d。", message, sessions.size()));
        sendMessage("客户端，你好！消息已经收到，现在群发消息……");
    }

    // 向客户端群发信息
    public void sendMessage(String message) {
//        LOGGER.debug(String.format("广播消息，%s，连接总数 %d。", message, sessions.size()));
        for (Session session : sessions) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
//                LOGGER.debug(e.getMessage());
            }
        }

    }

}