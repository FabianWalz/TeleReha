package ehealth.telereha.utils;

import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SessionStore {
    private static final ConcurrentMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public static void addSession(String email, WebSocketSession session) {
        sessions.put(email, session);
    }

    public static void removeSession(WebSocketSession session) {
        sessions.values().remove(session);
    }

    public static WebSocketSession getSession(String email) {
        return sessions.get(email);
    }
}
