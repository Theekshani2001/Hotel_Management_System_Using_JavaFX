package com.tharindi.hotel_vista.model;

public class Room {
    private String id;
    private String roomType;
    private String roomStatus;
    private String floorNumber;

    public Room() {
    }

    public Room(String id, String roomType, String roomStatus, String floorNumber) {
        this.id = id;
        this.roomType = roomType;
        this.roomStatus = roomStatus;
        this.floorNumber = floorNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(String roomStatus) {
        this.roomStatus = roomStatus;
    }

    public String getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(String floorNumber) {
        this.floorNumber = floorNumber;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id='" + id + '\'' +
                ", roomType='" + roomType + '\'' +
                ", roomStatus='" + roomStatus + '\'' +
                ", floorNumber='" + floorNumber + '\'' +
                '}';
    }
}
