//package com.robot.homeobot.services.sockets;
//
//import org.springframework.stereotype.Component;
//
//import javax.websocket.OnClose;
//import javax.websocket.OnMessage;
//import javax.websocket.OnOpen;
//import javax.websocket.Session;
//import javax.websocket.server.ServerEndpoint;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.CopyOnWriteArraySet;
//
//@Component
//@ServerEndpoint("/websocket")
//// This annotation is equivalent to setting access URLs
//public class WebSocket {
//
//    private Session session;
//
//    private static CopyOnWriteArraySet<WebSocket> webSockets =new CopyOnWriteArraySet<>();
//    private static Map<String,Session> sessionPool = new HashMap<String,Session>();
//
//    @OnOpen
//    public void onOpen(Session session) {
//        this.session = session;
//        webSockets.add(this);
//        System.out.println ("[websocket message] has new connections, total: " + webSockets.size());
//    }
//
//    @OnClose
//    public void onClose() {
//        webSockets.remove(this);
//        System.out.println ("[websocket message] disconnected, total: " + webSockets.size());
//    }
//
//    @OnMessage
//    public void onMessage(String message) {
//        System.out.println ("[websocket message] receives client message:"+message);
//    }
//
//    // This is a broadcast message.
//    public void sendAllMessage(String message) {
//        for(WebSocket webSocket : webSockets) {
//            System.out.println ("[websocket message] broadcast message: "+ message);
//            try {
//                webSocket.session.getAsyncRemote().sendText(message);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
////    // This is a single message
////    public void sendOneMessage(String shopId, String message) {
////        Session session = sessionPool.get(shopId);
////        if (session != null) {
////            try {
////                session.getAsyncRemote().sendText(message);
////            } catch (Exception e) {
////                e.printStackTrace();
////            }
////        }
////    }
//
//}