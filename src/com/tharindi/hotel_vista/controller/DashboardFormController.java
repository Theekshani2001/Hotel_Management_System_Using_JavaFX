package com.tharindi.hotel_vista.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DashboardFormController {

    public AnchorPane context;
    public Label lblDate;
    public Label lblTime;
    
    public void initialize(){
        setData();
    }

    private void setData() {
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String textDate=simpleDateFormat.format(date);
        lblDate.setText(textDate);

        //lblTime.setText(new SimpleDateFormat("HH:mm:ss").format(new Date()));

        Timeline timeline= new Timeline(
                new KeyFrame(Duration.seconds(0),
                e ->{
                    DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("hh:mm:ss");
                    lblTime.setText(LocalTime.now().format(dateTimeFormatter));
                }
                ),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

    }

    public void logoutOnAction(ActionEvent actionEvent) throws IOException {
        setUI("LoginForm");
    }

    private void setUI(String location) throws IOException {
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        stage.centerOnScreen();
    }


    public void openReservationFormOnAction(ActionEvent actionEvent) throws IOException {
        setUI("ReservationForm");
    }

    public void openRoomFormOnAction(ActionEvent actionEvent) throws IOException {
        setUI("RoomForm");
    }


    public void openBuffetTicketFormOnAction(ActionEvent actionEvent) throws IOException {
        setUI("BuffetTicketForm");
    }

    public void openPayrollFormOnAction(ActionEvent actionEvent) throws IOException {
        setUI("PayrollForm");
    }

    public void openUserFormOnAction(ActionEvent actionEvent) throws IOException {
        setUI("UserForm");
    }

    public void openCleaningFormOnAction(ActionEvent actionEvent) throws IOException {
        setUI("CleaningForm");
    }
}
