package com.tharindi.hotel_vista.model;

public class Payroll {
    private String id;
    private String employeeName;
    private String netPay;
    private String employeeType;

    public Payroll() {
    }

    public Payroll(String id, String employeeName, String netPay, String employeeType) {
        this.id = id;
        this.employeeName = employeeName;
        this.netPay = netPay;
        this.employeeType = employeeType;
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

    @Override
    public String toString() {
        return "Payroll{" +
                "id='" + id + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", netPay='" + netPay + '\'' +
                ", employeeType='" + employeeType + '\'' +
                '}';
    }
}
