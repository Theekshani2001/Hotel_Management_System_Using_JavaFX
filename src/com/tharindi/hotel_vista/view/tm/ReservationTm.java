package com.tharindi.hotel_vista.view.tm;

import javafx.scene.control.Button;

import java.util.Date;

public class ReservationTm {

    private String id;
    private String userName;
    private String roomType;
    private String checkInDate;
    private String checkOutDate;
    private String totalCost;
    private Button button;

    public ReservationTm() {
    }

    public ReservationTm(String id, String userName, String roomType, String checkInDate, String checkOutDate, String totalCost, Button button) {
        this.id = id;
        this.userName = userName;
        this.roomType = roomType;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalCost = totalCost;
        this.button = button;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    @Override
    public String toString() {
        return "ReservationTm{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", roomType='" + roomType + '\'' +
                ", checkInDate='" + checkInDate + '\'' +
                ", checkOutDate='" + checkOutDate + '\'' +
                ", totalCost='" + totalCost + '\'' +
                ", button=" + button +
                '}';
    }
}
