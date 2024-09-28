package com.tharindi.hotel_vista.view.tm;

import javafx.scene.control.Button;

public class UserTm {
    private String id;
    private String name;
    private String phone;
    private String address;
    private Button btn;

    public UserTm() {
    }

    public UserTm(String id, String name, String phone, String address, Button btn) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.btn = btn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }

    @Override
    public String toString() {
        return "UserTm{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", btn=" + btn +
                '}';
    }
}
