package com.tharindi.hotel_vista.view.tm;

import javafx.scene.control.Button;

public class RoomTm {

    private String id;
    private String roomType;
    private String roomStatus;
    private String floorNumber;
    private Button btn;

    public RoomTm() {
    }

    public RoomTm(String id, String roomType, String roomStatus, String floorNumber, Button btn) {
        this.id = id;
        this.roomType = roomType;
        this.roomStatus = roomStatus;
        this.floorNumber = floorNumber;
        this.btn = btn;
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

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }

    @Override
    public String toString() {
        return "RoomTm{" +
                "id='" + id + '\'' +
                ", roomType='" + roomType + '\'' +
                ", roomStatus='" + roomStatus + '\'' +
                ", floorNumber='" + floorNumber + '\'' +
                ", btn=" + btn +
                '}';
    }
}
