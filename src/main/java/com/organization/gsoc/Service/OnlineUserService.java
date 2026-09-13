package com.organization.gsoc.Service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OnlineUserService {

    private final Map<String, Set<String>> userSessions =
            new ConcurrentHashMap<>();

    public void connect(
            String userId,
            String sessionId
    ) {
        userSessions
                .computeIfAbsent(
                        userId,
                        key -> ConcurrentHashMap.newKeySet()
                )
                .add(sessionId);
    }

    public void disconnect(
            String userId,
            String sessionId
    ) {
        Set<String> sessions =
                userSessions.get(userId);

        if (sessions == null) {
            return;
        }

        sessions.remove(sessionId);

        if (sessions.isEmpty()) {
            userSessions.remove(userId);
        }
    }

    public int getOnlineCount() {
        return userSessions.size();
    }
}