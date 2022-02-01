package cf.mgorski.networkprogramming.task3.control_protocol;

import cf.mgorski.networkprogramming.task3.Message;

import java.util.List;

public class ChatResponse implements Response {
    List<Message> messages;

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public String getType() {
        return "chat";
    }
}