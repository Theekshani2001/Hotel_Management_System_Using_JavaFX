package com.tharindi.hotel_vista.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainPageController {
    public BorderPane bp;
    @FXML
    public AnchorPane ap;
    public AnchorPane context;



    public void goHome(ActionEvent actionEvent) throws IOException {

        bp.setCenter(ap);
    }

    public void goRooms(ActionEvent actionEvent) {
        loadPage("RoomPage");
    }

    public void goContact(ActionEvent actionEvent) {
        loadPage("ContactPage");
    }

    public void goGallery(ActionEvent actionEvent) {
        loadPage("GalleryPage");
    }

    private void loadPage(String page){
        Parent root=null;

        try {
            root= FXMLLoader.load(getClass().getResource("../view/"+page+".fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        bp.setCenter(root);
    }

    private void setUI(String location) throws IOException {
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        stage.centerOnScreen();
    }

    public void login(ActionEvent actionEvent) throws IOException {
        setUI("LoginForm");
    }
}
