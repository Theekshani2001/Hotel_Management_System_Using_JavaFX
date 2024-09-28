package com.tharindi.hotel_vista.db;

import com.tharindi.hotel_vista.model.Employee;
import com.tharindi.hotel_vista.model.User;
import com.tharindi.hotel_vista.util.security.PasswordManager;

import java.util.ArrayList;

public class Database {

    public static ArrayList<Employee> employeeTable= new ArrayList();
    public static ArrayList<User> userTable= new ArrayList();

    static {
        employeeTable.add(new Employee("Tharindi","0763741869","tharindi@gmail.com",new PasswordManager().encrypt("1234")));

    }
}
