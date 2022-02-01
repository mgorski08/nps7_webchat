package cf.mgorski.networkprogramming.task3.control_protocol;

import cf.mgorski.networkprogramming.task3.Room;

import java.util.Set;
import java.util.stream.Collectors;

public class RoomResponse implements Response {
    Set<String> rooms;

    public RoomResponse() {

    }

    public RoomResponse(Set<Room> roomList) {
        rooms = roomList.stream().map(Room::getName).collect(Collectors.toSet());
    }

    public Set<String> getRooms() {
        return rooms;
    }

    public void setRooms(Set<String> rooms) {
        this.rooms = rooms;
    }

    @Override
    public String getType() {
        return "rooms";
    }
}