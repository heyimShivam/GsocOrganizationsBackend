package com.organization.gsoc.Service;

import com.organization.gsoc.DTO.ChatChannelResponse;
import com.organization.gsoc.DTO.ChatMessageResponse;
import com.organization.gsoc.DTO.SendChatMessageRequest;
import com.organization.gsoc.Entity.ChatChannelEntity;
import com.organization.gsoc.Entity.ChatMessageEntity;
import com.organization.gsoc.Entity.UserEntity;
import com.organization.gsoc.Exception.AuthenticatedUserNotFoundException;
import com.organization.gsoc.Repository.ChatChannelRepository;
import com.organization.gsoc.Repository.ChatMessageRepository;
import com.organization.gsoc.Repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ChatServiceImpl implements ChatService {

    private final ChatChannelRepository chatChannelRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    public ChatServiceImpl(
            ChatChannelRepository chatChannelRepository,
            ChatMessageRepository chatMessageRepository,
            UserRepository userRepository
    ) {
        this.chatChannelRepository = chatChannelRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatChannelResponse> getChannels() {

        return chatChannelRepository.findAll()
                .stream()
                .map(channel ->
                        new ChatChannelResponse(
                                channel.getId(),
                                channel.getName(),
                                channel.getDescription()
                        )
                )
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatMessageResponse> getRecentMessages(
            UUID channelId
    ) {

        if (!chatChannelRepository.existsById(channelId)) {
            throw new RuntimeException(
                    "Chat channel not found"
            );
        }

        return chatMessageRepository
                .findTop50ByChannelIdOrderByCreatedAtDesc(
                        channelId
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ChatMessageResponse sendMessage(
            String email,
            SendChatMessageRequest request
    ) {

        UserEntity user =
                userRepository.findByEmail(
                        email.trim().toLowerCase()
                ).orElseThrow(() ->
                        new AuthenticatedUserNotFoundException(
                                "Authenticated user not found"
                        )
                );

        ChatChannelEntity channel =
                chatChannelRepository.findById(
                        request.channelId()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Chat channel not found"
                        )
                );

        String message = request.message().trim();

        if (message.isBlank()) {
            throw new IllegalArgumentException(
                    "Message cannot be empty"
            );
        }

        ChatMessageEntity chatMessage =
                new ChatMessageEntity();

        chatMessage.setChannel(channel);
        chatMessage.setUser(user);
        chatMessage.setMessage(message);

        ChatMessageEntity saved =
                chatMessageRepository.save(chatMessage);

        return toResponse(saved);
    }

    private ChatMessageResponse toResponse(
            ChatMessageEntity entity
    ) {

        UserEntity user = entity.getUser();

        return new ChatMessageResponse(
                entity.getId(),
                entity.getChannel().getId(),
                user.getId(),
                user.getName(),
                user.getGithubUsername(),
                entity.getMessage(),
                entity.getCreatedAt()
        );
    }
}