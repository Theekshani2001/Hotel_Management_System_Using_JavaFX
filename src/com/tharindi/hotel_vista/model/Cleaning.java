package com.tharindi.hotel_vista.model;

import java.util.Date;

public class Cleaning {
    private String id;
    private Date cleaningDate;
    private String cleaningStatus;
    private String roomNumber;

    public Cleaning() {
    }

    public Cleaning(String id, Date cleaningDate, String cleaningStatus, String roomNumber) {
        this.id = id;
        this.cleaningDate = cleaningDate;
        this.cleaningStatus = cleaningStatus;
        this.roomNumber = roomNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCleaningDate() {
        return cleaningDate;
    }

    public void setCleaningDate(Date cleaningDate) {
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
        this.roomNumber = roomNumber;
    }

    @Override
    public String toString() {
        return "Cleaning{" +
                "id='" + id + '\'' +
                ", cleaningDate=" + cleaningDate +
                ", cleaningStatus='" + cleaningStatus + '\'' +
                ", roomNumber='" + roomNumber + '\'' +
                '}';
    }
}
