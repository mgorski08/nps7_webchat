package cf.mgorski.networkprogramming.task3.control_protocol;

import cf.mgorski.networkprogramming.task3.Message;

public class MessageResponse implements Response{
    String senderNick;
    String text;

    public MessageResponse() {

    }

    public MessageResponse(Message message) {
        senderNick = message.getSenderNick();
        text = message.getText();
    }

    public String getSenderNick() {
        return senderNick;
    }

    public void setSenderNick(String senderNick) {
        this.senderNick = senderNick;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String getType() {
        return "message";
    }
}