package com.organization.gsoc.Controller;

import com.organization.gsoc.DTO.ChatChannelResponse;
import com.organization.gsoc.DTO.ChatMessageResponse;
import com.organization.gsoc.Service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class ChatControllerImpl implements ChatController {

    private final ChatService chatService;

    public ChatControllerImpl(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public ResponseEntity<List<ChatChannelResponse>> getChannels(
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                chatService.getChannels()
        );
    }

    @Override
    public ResponseEntity<List<ChatMessageResponse>> getMessages(
            Authentication authentication,
            UUID channelId
    ) {

        return ResponseEntity.ok(
                chatService.getRecentMessages(channelId)
        );
    }
}