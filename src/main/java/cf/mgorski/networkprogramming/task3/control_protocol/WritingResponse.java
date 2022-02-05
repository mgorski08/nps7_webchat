package cf.mgorski.networkprogramming.task3.control_protocol;

public class WritingResponse implements Response {
    String participant;

    public WritingResponse() {

    }

    public WritingResponse(String participant) {
        this.participant = participant;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    @Override
    public String getType() {
        return "writing";
    }
}