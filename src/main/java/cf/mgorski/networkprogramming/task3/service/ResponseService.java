package cf.mgorski.networkprogramming.task3.service;

import cf.mgorski.networkprogramming.task3.Participant;
import cf.mgorski.networkprogramming.task3.Room;
import cf.mgorski.networkprogramming.task3.control_protocol.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Service
public class ResponseService {
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    SessionManager sessionManager;

    public void sendResponse(WebSocketSession webSocketSession, Response response) throws IOException {
        String messageString = mapper.writeValueAsString(response);
        webSocketSession.sendMessage(new TextMessage(messageString));
    }

    public void sendResponseToRoom(String room, Response response) throws IOException {
        for (Participant p : sessionManager.getParticipantsByRoomName(room)) {
            sendResponse(p.getWebSocketSession(), response);
        }
    }
}
