package com.organization.gsoc.WebSocket;

import com.organization.gsoc.DTO.ChatMessageResponse;
import com.organization.gsoc.DTO.SendChatMessageRequest;
import com.organization.gsoc.Service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatWebSocketController(
            ChatService chatService,
            SimpMessagingTemplate messagingTemplate
    ) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.send")
    public void sendMessage(
            SendChatMessageRequest request,
            Authentication authentication
    ) {

        if (authentication == null) {
            throw new IllegalStateException(
                    "User is not authenticated"
            );
        }

        ChatMessageResponse response =
                chatService.sendMessage(
                        authentication.getName(),
                        request
                );

        messagingTemplate.convertAndSend(
                "/topic/channel/" + request.channelId(),
                response
        );
    }
}