package cf.mgorski.networkprogramming.task3;

import java.util.ArrayList;
import java.util.List;

public class Room {
    String name;
    List<Message> messageList = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Message> getMessages() {
        return messageList;
    }

    public void addMessage(Message message) {
        messageList.add(message);
    }
}
