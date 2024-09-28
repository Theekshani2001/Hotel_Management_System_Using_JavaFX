package com.tharindi.hotel_vista.view.tm;

import javafx.scene.control.Button;

public class PayrollTm {
    private String id;
    private String employeeName;
    private String netPay;
    private String employeeType;
    private Button button;

    public PayrollTm() {
    }

    public PayrollTm(String id, String employeeName, String netPay, String employeeType, Button button) {
        this.id = id;
        this.employeeName = employeeName;
        this.netPay = netPay;
        this.employeeType = employeeType;
        this.button = button;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getNetPay() {
        return netPay;
    }

    public void setNetPay(String netPay) {
        this.netPay = netPay;
    }

    public String getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    @Override
    public String toString() {
        return "PayrollTm{" +
                "id='" + id + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", netPay='" + netPay + '\'' +
                ", employeeType='" + employeeType + '\'' +
                ", button=" + button +
                '}';
    }
}
