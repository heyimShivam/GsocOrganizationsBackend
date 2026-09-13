package com.organization.gsoc.Config;

import jakarta.servlet.http.HttpSession;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.Principal;

public class WebSocketAuthInterceptor
        implements ChannelInterceptor {

    @Override
    public Message<?> preSend(
            Message<?> message,
            MessageChannel channel
    ) {

        StompHeaderAccessor accessor =
                StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            Object sessionObject =
                    accessor.getSessionAttributes()
                            .get("HTTP_SESSION");

            if (sessionObject instanceof HttpSession session) {

                Object securityContext =
                        session.getAttribute(
                                "SPRING_SECURITY_CONTEXT"
                        );

                if (securityContext instanceof
                        org.springframework.security.core.context.SecurityContext context) {

                    Principal principal =
                            context.getAuthentication();

                    if (principal != null) {

                        accessor.setUser(principal);
                    }
                }
            }
        }

        return message;
    }
}