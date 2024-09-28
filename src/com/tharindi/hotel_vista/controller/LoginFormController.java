package com.tharindi.hotel_vista.controller;

import com.tharindi.hotel_vista.db.Database;
import com.tharindi.hotel_vista.db.DbConnection;
import com.tharindi.hotel_vista.model.Employee;
import com.tharindi.hotel_vista.util.security.PasswordManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class LoginFormController {


    public AnchorPane context;
    public TextField txtEmail;
    public PasswordField txtPassword;

    public void loginOnAction(ActionEvent actionEvent) throws IOException {

        String email = txtEmail.getText().toLowerCase();
        String password = txtPassword.getText().trim();


        try {
            Employee selectedEmployee = login(email);
            if (null!=selectedEmployee){
                if (new PasswordManager().checkPassword(password,selectedEmployee.getPassword())){
                    System.out.println(selectedEmployee.toString());
                    setUI("DashboardForm");
                }else {
                    new Alert(Alert.AlertType.WARNING,"Wrong Password").show();
                }

            }else {
                new Alert(Alert.AlertType.WARNING,String.format("Employee Not Found (%s)",email)).show();
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.toString());
            new Alert(Alert.AlertType.ERROR,e.toString()).show();
        }


    }

    public void forgotPasswordOnAction(ActionEvent actionEvent) {
    }

    public void createAccountOnAction(ActionEvent actionEvent) throws IOException {
        setUI("SignupForm");
    }

    private void setUI(String location) throws IOException {
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        stage.centerOnScreen();
    }

//    private Employee login(String email) throws ClassNotFoundException, SQLException {
//        Class.forName("com.mysql.cj.jdbc.Driver");
//        Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/new_hotel_vista","root","mysql");
//
//        String sql="SELECT * FROM employee WHERE email=?";
//        PreparedStatement statement=connection.prepareStatement(sql);
//        statement.setString(1,email);
//        ResultSet resultSet = statement.executeQuery(sql);
//        if (resultSet.next()){
//            Employee employee=new Employee(
//                    resultSet.getString("email"),
//                    resultSet.getString("name"),
//                    resultSet.getString("phone"),
//                    resultSet.getString("password")
//            );
//            System.out.println(employee.toString());
//            return employee;
//        }
//        return null;
//
//    }

    private Employee login(String email) throws ClassNotFoundException, SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "SELECT * FROM employee WHERE email=?";  // Prepared statement with placeholder
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, email);  // Set the value for the placeholder

        ResultSet resultSet = statement.executeQuery();  // Execute the prepared statement

        if (resultSet.next()) {
            Employee employee = new Employee(
                    resultSet.getString("email"),
                    resultSet.getString("name"),
                    resultSet.getString("phone"),
                    resultSet.getString("password")
            );
            System.out.println(employee.toString());
            return employee;
        }
        return null;

    }

}
