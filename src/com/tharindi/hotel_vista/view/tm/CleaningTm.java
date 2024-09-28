package com.tharindi.hotel_vista.view.tm;

import javafx.scene.control.Button;

import java.util.Date;

public class CleaningTm {
    private String id;
    private String cleaningDate;
    private String cleaningStatus;
    private String roomNumber;
    private Button button;

    public CleaningTm() {
    }

    public CleaningTm(String id, String cleaningDate, String cleaningStatus, String roomNumber, Button button) {
        this.id = id;
        this.cleaningDate = cleaningDate;
        this.cleaningStatus = cleaningStatus;
        this.roomNumber = roomNumber;
        this.button = button;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCleaningDate() {
        return cleaningDate;
    }

    public void setCleaningDate(String cleaningDate) {
        this.cleaningDate = cleaningDate;
    }

    public String getCleaningStatus() {
        return cleaningStatus;
    }

    public void setCleaningStatus(String cleaningStatus) {
        this.cleaningStatus = cleaningStatus;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomType) {
        this.roomNumber = roomType;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    @Override
    public String toString() {
        return "CleaningTm{" +
                "id='" + id + '\'' +
                ", cleaningDate='" + cleaningDate + '\'' +
                ", cleaningStatus='" + cleaningStatus + '\'' +
                ", roomNumber='" + roomNumber + '\'' +
                ", button=" + button +
                '}';
    }
}
