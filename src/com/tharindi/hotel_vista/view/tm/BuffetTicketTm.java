package com.tharindi.hotel_vista.view.tm;

import javafx.scene.control.Button;

import java.time.LocalDate;
import java.util.Date;

public class BuffetTicketTm {

    private String id;
    private String issueDate;
    private String ticketType;
    private String price;
    private String userName;
    private Button btn;

    public BuffetTicketTm() {
    }

    public BuffetTicketTm(String id, String issueDate, String ticketType, String price, String userName, Button btn) {
        this.id = id;
        this.issueDate = issueDate;
        this.ticketType = ticketType;
        this.price = price;
        this.userName = userName;
        this.btn = btn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
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

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }

    @Override
    public String toString() {
        return "BuffetTicketTm{" +
                "id='" + id + '\'' +
                ", issueDate=" + issueDate +
                ", ticketType='" + ticketType + '\'' +
                ", price='" + price + '\'' +
                ", userName='" + userName + '\'' +
                ", btn=" + btn +
                '}';
    }
}
