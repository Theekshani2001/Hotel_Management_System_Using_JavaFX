package com.tharindi.hotel_vista.model;

import java.time.LocalDate;
import java.util.Date;

public class BuffetTicket {
    private String id;
    private Date issueDate;
    private String ticketType;
    private String price;
    private String userName;

    public BuffetTicket() {
    }

    public BuffetTicket(String id, Date issueDate, String ticketType, String price, String userName) {
        this.id = id;
        this.issueDate = issueDate;
        this.ticketType = ticketType;
        this.price = price;
        this.userName = userName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "BuffetTicket{" +
                "id='" + id + '\'' +
                ", issueDate=" + issueDate +
                ", ticketType='" + ticketType + '\'' +
                ", price='" + price + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
