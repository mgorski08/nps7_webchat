package cf.mgorski.networkprogramming.task3.service;

import cf.mgorski.networkprogramming.task3.Participant;
import cf.mgorski.networkprogramming.task3.Room;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;

@Service
public class SessionManager {
    private final Log logger = LogFactory.getLog(this.getClass());
    private final Set<WebSocketSession> waitingRoom = Collections.synchronizedSet(new HashSet<>());
    private final Set<Participant> participants = Collections.synchronizedSet(new HashSet<>());

//    public List<String> getParticipantsInRoom(String room) {
//        return participants.get(room);
//    }

    public void addWaitingConnection(WebSocketSession webSocketSession) {
        waitingRoom.add(webSocketSession);
        logger.info("Client connected");
    }

    public void dropConnection(WebSocketSession webSocketSession) {
        Optional<Participant> maybeParticipant = getParticipantBySession(webSocketSession);
        maybeParticipant.ifPresent(this::leaveRoom);
        waitingRoom.remove(webSocketSession);
        maybeParticipant.ifPresentOrElse(
                p -> logger.info(p.getNick() + " disconnected"),
                () -> logger.info("Client disconnected")
        );
    }

    public void joinRoom(WebSocketSession webSocketSession, String nick, Room room) {
        waitingRoom.remove(webSocketSession);
        Participant participant = new Participant();
        participant.setNick(nick);
        participant.setWebSocketSession(webSocketSession);
        participant.setRoom(room);
        participants.add(participant);
        logger.info(nick + " has joined room " + room.getName());
    }

    public void leaveRoom(Participant participant) {
        participants.remove(participant);
        waitingRoom.add(participant.getWebSocketSession());
        logger.info(participant.getNick() + " has left room " + participant.getRoom().getName());
    }

    public Optional<Participant> getParticipantByNick(String nick) {
        synchronized (participants) {
            for (Participant p : participants) {
                if (p.getNick().equals(nick)) {
                    return Optional.of(p);
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Participant> getParticipantBySession(WebSocketSession webSocketSession) {
        synchronized (participants) {
            for (Participant p : participants) {
                if (p.getWebSocketSession().equals(webSocketSession)) {
                    return Optional.of(p);
                }
            }
        }
        return Optional.empty();
    }

    public Set<Participant> getParticipantsByRoomName(String roomName) {
        Set<Participant> retval = new HashSet<>();
        synchronized (participants) {
            for (Participant p : participants) {
                if (p.getRoom().getName().equals(roomName)) {
                    retval.add(p);
                }
            }
        }
        return retval;
    }

    public Room getRoomByName(String name) {
        synchronized (participants) {
            for (Participant p : participants) {
                if (p.getRoom().getName().equals(name)) {
                    return p.getRoom();
                }
            }
        }
        Room room = new Room();
        room.setName(name);
        return room;
    }

    public Set<Room> getRooms() {
        Set<Room> retval = new HashSet<>();
        synchronized (participants) {
            for (Participant p : participants) {
                retval.add(p.getRoom());
            }
        }
        return retval;
    }
}
