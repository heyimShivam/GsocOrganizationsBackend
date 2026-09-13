package com.organization.gsoc.WebSocket;

import com.organization.gsoc.DTO.OnlineUserCountResponse;
import com.organization.gsoc.Service.OnlineUserService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.security.Principal;

@Component
public class WebSocketEventListener {

    private final OnlineUserService onlineUserService;
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketEventListener(
            OnlineUserService onlineUserService,
            SimpMessagingTemplate messagingTemplate
    ) {
        this.onlineUserService = onlineUserService;
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleWebSocketConnect(
            SessionConnectedEvent event
    ) {
        StompHeaderAccessor accessor =
                StompHeaderAccessor.wrap(event.getMessage());

        Principal principal = accessor.getUser();

        if (principal == null) {
            System.out.println(
                    "WebSocket connected but principal is null"
            );
            return;
        }

        String userId = principal.getName();
        String sessionId = accessor.getSessionId();

        System.out.println(
                "WebSocket user connected: " + userId
        );

        onlineUserService.connect(
                userId,
                sessionId
        );

        broadcastOnlineCount();
    }

    @EventListener
    public void handleSubscription(
            SessionSubscribeEvent event
    ) {
        StompHeaderAccessor accessor =
                StompHeaderAccessor.wrap(event.getMessage());

        String destination =
                accessor.getDestination();

        if (!"/topic/online-count".equals(destination)) {
            return;
        }

        System.out.println(
                "Online count subscription received"
        );

        /*
         * Important:
         *
         * The client has now subscribed, so broadcast
         * the current count AFTER the subscription exists.
         *
         * This guarantees the newly connected client
         * receives the current count.
         */
        broadcastOnlineCount();
    }

    @EventListener
    public void handleWebSocketDisconnect(
            SessionDisconnectEvent event
    ) {
        StompHeaderAccessor accessor =
                StompHeaderAccessor.wrap(event.getMessage());

        Principal principal = accessor.getUser();

        if (principal == null) {
            System.out.println(
                    "WebSocket disconnected but principal is null"
            );
            return;
        }

        String userId = principal.getName();
        String sessionId = accessor.getSessionId();

        System.out.println(
                "WebSocket user disconnected: " + userId
        );

        onlineUserService.disconnect(
                userId,
                sessionId
        );

        broadcastOnlineCount();
    }

    private void broadcastOnlineCount() {

        int count =
                onlineUserService.getOnlineCount();

        System.out.println(
                "Broadcasting online count: " + count
        );

        messagingTemplate.convertAndSend(
                "/topic/online-count",
                new OnlineUserCountResponse(count)
        );
    }
}