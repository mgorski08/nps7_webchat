package cf.mgorski.networkprogramming.task3.controller;

import cf.mgorski.networkprogramming.task3.control_protocol.ClientCommand;
import cf.mgorski.networkprogramming.task3.service.CommandExecutionService;
import cf.mgorski.networkprogramming.task3.service.SessionManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class ChatMessageHandler extends TextWebSocketHandler {

    @Autowired
    CommandExecutionService commandExecutionService;

    @Autowired
    SessionManager sessionManager;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        sessionManager.addWaitingConnection(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        sessionManager.dropConnection(session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        super.handleMessage(session, message);
        ObjectMapper mapper = new ObjectMapper();
        ClientCommand m = mapper.readValue((String) message.getPayload(), ClientCommand.class);
        commandExecutionService.executeMessage(m, session);
    }
}
