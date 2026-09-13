package com.organization.gsoc.Service;

import com.organization.gsoc.DTO.ChatChannelResponse;
import com.organization.gsoc.DTO.ChatMessageResponse;
import com.organization.gsoc.DTO.SendChatMessageRequest;

import java.util.List;
import java.util.UUID;

public interface ChatService {

    List<ChatChannelResponse> getChannels();

    List<ChatMessageResponse> getRecentMessages(UUID channelId);

    ChatMessageResponse sendMessage(
            String email,
            SendChatMessageRequest request
    );
}