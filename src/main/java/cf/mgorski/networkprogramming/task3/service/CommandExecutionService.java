package cf.mgorski.networkprogramming.task3.service;

import cf.mgorski.networkprogramming.task3.Message;
import cf.mgorski.networkprogramming.task3.Participant;
import cf.mgorski.networkprogramming.task3.Room;
import cf.mgorski.networkprogramming.task3.control_protocol.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
public class CommandExecutionService {
    private final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    SessionManager sessionManager;

    @Autowired
    ResponseService responseService;

    public void executeMessage(ClientCommand clientCommand, WebSocketSession webSocketSession) throws IOException {
        switch (clientCommand.getOperation()) {
            case Operation.MESSAGE:
                handleMessage(clientCommand, webSocketSession);
                break;
            case Operation.JOIN:
                handleJoin(clientCommand, webSocketSession);
                break;
            case Operation.LEAVE:
                handleLeave(clientCommand, webSocketSession);
                break;
            case Operation.LIST_ROOMS:
                handleListRooms(clientCommand, webSocketSession);
                break;
            case Operation.GET_CHAT:
                handleGetChat(clientCommand, webSocketSession);
                break;
            case Operation.GET_PARTICIPANTS:
                handleGetParticipants(clientCommand, webSocketSession);
                break;
        }
    }

    public void handleMessage(ClientCommand clientCommand, WebSocketSession webSocketSession) throws IOException {
        Participant sender = sessionManager.getParticipantBySession(webSocketSession).get();
        String text = clientCommand.getArgument1();
        logger.info(String.format("[%s] %s: %s", sender.getRoom().getName(), sender.getNick(), text));
        Message message = new Message();
        message.setSenderNick(sender.getNick());
        message.setText(text);
        sender.getRoom().addMessage(message);
        responseService.sendResponseToRoom(sender.getRoom().getName(), new MessageResponse(message));
    }

    public void handleJoin(ClientCommand clientCommand, WebSocketSession webSocketSession) throws IOException {
        Room room = sessionManager.getRoomByName(clientCommand.getArgument2());
        sessionManager.joinRoom(webSocketSession, clientCommand.getArgument1(), room);

        Set<Participant> participants = sessionManager.getParticipantsByRoomName(room.getName());
        responseService.sendResponseToRoom(room.getName(), new ParticipantResponse(participants));
    }

    public void handleLeave(ClientCommand clientCommand, WebSocketSession webSocketSession) throws IOException {
        Participant participant = sessionManager.getParticipantBySession(webSocketSession).get();
        sessionManager.leaveRoom(participant);
        Set<Participant> participants = sessionManager.getParticipantsByRoomName(participant.getRoom().getName());
        responseService.sendResponseToRoom(participant.getRoom().getName(), new ParticipantResponse(participants));
    }

    public void handleListRooms(ClientCommand clientCommand, WebSocketSession webSocketSession) throws IOException {
        Set<Room> rooms = sessionManager.getRooms();
        responseService.sendResponse(webSocketSession, new RoomResponse(rooms));
    }

    public void handleGetChat(ClientCommand clientCommand, WebSocketSession webSocketSession) throws IOException {
        Participant sender = sessionManager.getParticipantBySession(webSocketSession).get();
        List<Message> messages = sender.getRoom().getMessages();
        ChatResponse chatResponse = new ChatResponse();
        chatResponse.setMessages(messages);
        responseService.sendResponse(webSocketSession, chatResponse);
    }

    public void handleGetParticipants(ClientCommand clientCommand, WebSocketSession webSocketSession) throws IOException {
        Participant sender = sessionManager.getParticipantBySession(webSocketSession).get();
        Set<Participant> participants = sessionManager.getParticipantsByRoomName(sender.getRoom().getName());
        responseService.sendResponse(webSocketSession, new ParticipantResponse(participants));
    }

}
