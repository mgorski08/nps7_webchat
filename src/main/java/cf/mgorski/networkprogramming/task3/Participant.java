package cf.mgorski.networkprogramming.task3;

import org.springframework.web.socket.WebSocketSession;

public class Participant {
    String nick;
    Room room;
    WebSocketSession webSocketSession;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public WebSocketSession getWebSocketSession() {
        return webSocketSession;
    }

    public void setWebSocketSession(WebSocketSession webSocketSession) {
        this.webSocketSession = webSocketSession;
    }
}
