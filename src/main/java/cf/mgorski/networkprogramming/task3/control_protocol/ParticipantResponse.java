package cf.mgorski.networkprogramming.task3.control_protocol;

import cf.mgorski.networkprogramming.task3.Participant;

import java.util.Set;
import java.util.stream.Collectors;

public class ParticipantResponse implements Response {
    Set<String> participants;

    public ParticipantResponse() {

    }

    public ParticipantResponse(Set<Participant> participantList) {
        participants = participantList.stream().map(Participant::getNick).collect(Collectors.toSet());
    }

    public Set<String> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<String> participants) {
        this.participants = participants;
    }

    @Override
    public String getType() {
        return "participants";
    }
}