package ehealth.telereha.controllers;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import ehealth.telereha.models.MsgInfo;
import ehealth.telereha.services.MessageService;
import ehealth.telereha.utils.SessionStore;

@Controller
public class MessageController extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private MessageService service;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String sessionId = session.getId();
        logger.info("WebSocket connection established with session id: {}", sessionId);
        String userEmail = session.getUri().getQuery().split("=")[1]; // Assume the query contains the email
        SessionStore.addSession(userEmail, session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        SessionStore.removeSession(session);
        logger.info("WebSocket connection closed with session id: {}", session.getId());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws IOException {
        String payload = message.getPayload().toString();
        logger.info("Received message: {}", payload);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(payload);

        if (actualObj.hasNonNull("msg") && !actualObj.get("msg").asText().isEmpty()) {
            String from = actualObj.get("from").asText();
            String to = actualObj.get("to").asText();
            String msg = actualObj.get("msg").asText();
            long timestamp = Calendar.getInstance().getTimeInMillis();

            MsgInfo msgInfo = new MsgInfo(from, to, msg, timestamp);
            service.save(msgInfo);
            updateClients(from, to, msgInfo);
        } else if (actualObj.hasNonNull("from") && actualObj.hasNonNull("to")) {
            String from = actualObj.get("from").asText();
            String to = actualObj.get("to").asText();
            sendAllMessages(from, to);
        }
    }

    private void sendAllMessages(String from, String to) throws IOException {
        List<MsgInfo> msgInfos = service.getMessages(from, to, 50);
        ObjectMapper responseMapper = new ObjectMapper();
        ObjectNode rootNode = responseMapper.createObjectNode();
        ArrayNode msgNodes = responseMapper.createArrayNode();
        for (MsgInfo msg : msgInfos) {
            msg.toJson(msgNodes.addObject());
        }
        rootNode.set("msgs", msgNodes);

        TextMessage responseMessage = new TextMessage(responseMapper.writeValueAsString(rootNode));
        WebSocketSession fromSession = SessionStore.getSession(from);
        WebSocketSession toSession = SessionStore.getSession(to);

        if (fromSession != null) {
            fromSession.sendMessage(responseMessage);
        }
        if (toSession != null) {
            toSession.sendMessage(responseMessage);
        }
    }

    private void updateClients(String from, String to, MsgInfo msgInfo) throws IOException {
        ObjectMapper responseMapper = new ObjectMapper();
        ObjectNode msgNode = responseMapper.createObjectNode();
        msgInfo.toJson(msgNode);

        TextMessage responseMessage = new TextMessage(responseMapper.writeValueAsString(msgNode));
        WebSocketSession fromSession = SessionStore.getSession(from);
        WebSocketSession toSession = SessionStore.getSession(to);

        if (fromSession != null) {
            fromSession.sendMessage(responseMessage);
        }
        if (toSession != null) {
            toSession.sendMessage(responseMessage);
        }
    }
}
