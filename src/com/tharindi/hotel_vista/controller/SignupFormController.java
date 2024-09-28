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

import java.io.IOException;
import java.sql.*;
import java.util.Locale;

public class SignupFormController {
    public AnchorPane context;
    public PasswordField txtPassword;
    public TextField txtName;
    public TextField txtPhoneNumber;
    public TextField txtEmail;

    public void signupOnAction(ActionEvent actionEvent) throws IOException {

        String email=txtEmail.getText().toLowerCase();
        String name=txtName.getText();
        String phoneNumber=txtPhoneNumber.getText();
        String password=new PasswordManager().encrypt(txtPassword.getText().trim());

        Employee createEmployee=new Employee(name,phoneNumber,email,password);

        try {
            boolean isSaved = signup(createEmployee);
            if (isSaved){
                new Alert(Alert.AlertType.INFORMATION,"Welcome!...").show();
                setUI("LoginForm");
            }else {
                new Alert(Alert.AlertType.WARNING,"Try Again!...").show();
            }
        } catch (ClassNotFoundException | SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.toString()).show();
        }




    }

    public void haveAccountOnAction(ActionEvent actionEvent) throws IOException {
        setUI("LoginForm");
    }

    private void setUI(String location) throws IOException {
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        stage.centerOnScreen();
    }

    private boolean signup(Employee employee) throws ClassNotFoundException, SQLException {
        Connection connection= DbConnection.getInstance().getConnection();
        String sql="INSERT INTO employee values (?,?,?,?)";
        PreparedStatement statement=connection.prepareStatement(sql);
        statement.setString(1,employee.getEmail());
        statement.setString(2,employee.getName());
        statement.setString(3,employee.getPhoneNumber());
        statement.setString(4,employee.getPassword());
        int rowCount = statement.executeUpdate();
        return rowCount>0;
    }
}
