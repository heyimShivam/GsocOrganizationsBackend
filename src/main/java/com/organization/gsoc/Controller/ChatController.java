package com.organization.gsoc.Controller;

import com.organization.gsoc.DTO.ChatChannelResponse;
import com.organization.gsoc.DTO.ChatMessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/chat")
public interface ChatController {

    @GetMapping("/channels")
    ResponseEntity<List<ChatChannelResponse>> getChannels(
            Authentication authentication
    );

    @GetMapping("/channels/{channelId}/messages")
    ResponseEntity<List<ChatMessageResponse>> getMessages(
            Authentication authentication,
            @PathVariable UUID channelId
    );
}