package com.tharindi.hotel_vista.controller;

import com.tharindi.hotel_vista.db.DbConnection;
import com.tharindi.hotel_vista.model.BuffetTicket;
import com.tharindi.hotel_vista.model.Reservation;
import com.tharindi.hotel_vista.view.tm.BuffetTicketTm;
import com.tharindi.hotel_vista.view.tm.ReservationTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ReservationFormController {
    public AnchorPane context;
    public TextField txtId;
    public TextField txtUserName;
    public TextField txtTotalCost;
    public TextField txtSearch;
    public Button btn;
    public TableView tblUsers;
    public TableColumn colId;
    public TableColumn colUserName;
    public TableColumn colRoomType;
    public TableColumn colCheckInDate;
    public TableColumn colCheckOutDate;
    public TableColumn colTotalCost;
    public TableColumn colOption;
    public ComboBox comboRoomType;
    public DatePicker txtCheckInDate;
    public DatePicker txtCheckOutDate;
    public TableView tblReservations;

    String searchText="";

    ObservableList<String> listRoomTypes= FXCollections.observableArrayList("Single Room","Double Room","King Size Room","Suit Room");


    public void initialize(){
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colRoomType.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        colCheckInDate.setCellValueFactory(new PropertyValueFactory<>("checkInDate"));
        colCheckOutDate.setCellValueFactory(new PropertyValueFactory<>("checkOutDate"));
        colTotalCost.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("button"));
        comboRoomType.setItems(listRoomTypes);
        setReservationId();
        setTableData(searchText);

        txtSearch.textProperty().addListener((observable,oldValue,newValue)->{
            searchText=newValue;
            setTableData(searchText);
        });

        tblReservations.getSelectionModel().selectedItemProperty().addListener((observable,oldValue,newValue)->{
            if (null!=newValue){
                setData((ReservationTm)newValue);
            }

        });
    }

    private void setReservationId() {
        try {
            String lastId = getLastId();
            if (null!=lastId){
                String splitData[] =lastId.split("-");
                String lastIdIntegerNumberAsString=splitData[1];
                int lastIdIntegerAsInt=Integer.parseInt(lastIdIntegerNumberAsString);
                lastIdIntegerAsInt++;
                String generatedUserId="R-"+lastIdIntegerAsInt;
                txtId.setText(generatedUserId);
            }else {
                txtId.setText("R-1");
            }
        } catch (ClassNotFoundException | SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.toString()).show();
            System.out.println(e.toString());
        }

    }

    private void setData(ReservationTm tm) {
        txtId.setText(tm.getId());
        txtUserName.setText(tm.getUserName());
        comboRoomType.setValue(tm.getRoomType());
        txtCheckInDate.setValue(LocalDate.parse(tm.getCheckInDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        txtCheckOutDate.setValue(LocalDate.parse(tm.getCheckOutDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        txtTotalCost.setText(tm.getTotalCost());
        btn.setText("Update Reservation");
    }

    private void setTableData(String searchText) {
        ObservableList<ReservationTm> obList= FXCollections.observableArrayList();
        try {
            for (Reservation reservation:searchReservations(searchText)){
                Button button = new Button("delete");
                ReservationTm reservationTm=new ReservationTm(
                        reservation.getId(),
                        reservation.getUserName(),
                        reservation.getRoomType(),
                        new SimpleDateFormat("yyyy-MM-dd").format(reservation.getCheckInDate()) ,
                        new SimpleDateFormat("yyyy-MM-dd").format(reservation.getCheckOutDate()) ,
                        reservation.getTotalCost(),
                        button
                );

                button.setOnAction(event -> {
                    Alert alert=new Alert(
                            Alert.AlertType.CONFIRMATION,"Are you Sure..?",
                            ButtonType.YES,
                            ButtonType.NO
                    );
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.get().equals(ButtonType.YES)){

                        try {
                            if (deleteReservation(reservation.getId())){
                                new Alert(Alert.AlertType.INFORMATION,"Deleted...!");
                                setTableData(searchText);
                                clear();
                                setReservationId();
                                btn.setText("Save Reservation");
                            }else {
                                new Alert(Alert.AlertType.WARNING,"Try Again!...").show();
                            }
                        }  catch (ClassNotFoundException | SQLException e) {
                            new Alert(Alert.AlertType.ERROR,e.toString()).show();
                        }

                    }

                });

                obList.add(reservationTm);

            }
            tblReservations.setItems(obList);

        } catch (ClassNotFoundException | SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.toString()).show();
            System.out.println(e.toString());
        }
    }

    public void saveOnAction(ActionEvent actionEvent) {

        if (!validateCheckoutDate()) {
            return; // If validation fails, stop further execution
        }

        Reservation reservation=new Reservation(
                txtId.getText(),
                txtUserName.getText(),
                comboRoomType.getValue().toString(),
                Date.from(txtCheckInDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Date.from(txtCheckOutDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                txtTotalCost.getText()

        );

        if (btn.getText().equalsIgnoreCase("Save Reservation")){
            try {
                if (saveReservation(reservation)){
                    System.out.println(reservation.toString());
                    setReservationId();
                    clear();
                    setTableData(searchText);
                    new Alert(Alert.AlertType.INFORMATION,"Reservation Saved!...").show();
                }else {

                    new Alert(Alert.AlertType.WARNING,"Try Again!...").show();
                }

            }  catch (ClassNotFoundException | SQLException e) {
                new Alert(Alert.AlertType.ERROR,e.toString()).show();
            }
        }else {

            try {
                if (updateReservation(reservation)){
                    System.out.println(reservation.toString());
                    setReservationId();
                    clear();
                    setTableData(searchText);
                    btn.setText("Save Reservation");
                    new Alert(Alert.AlertType.INFORMATION,"Reservation Updated!...").show();
                }else {
                    new Alert(Alert.AlertType.WARNING,"Try Again!456...").show();
                }

            }  catch (ClassNotFoundException | SQLException e) {
                new Alert(Alert.AlertType.ERROR,e.toString()).show();
            }
        }

    }

    private boolean validateCheckoutDate() {
        if (txtCheckInDate.getValue() == null || txtCheckOutDate.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "Please select both check-in and check-out dates").show();
            return false;
        }

        LocalDate checkInDate = txtCheckInDate.getValue();
        LocalDate checkOutDate = txtCheckOutDate.getValue();

        if (checkOutDate.isBefore(checkInDate)) {
            new Alert(Alert.AlertType.WARNING, "Check-out date must be the same or after the check-in date").show();
            return false;
        }
        return true;
    }


    private void clear(){

        txtCheckInDate.setValue(null);
        txtCheckOutDate.setValue(null);
        comboRoomType.setValue(null);
        txtTotalCost.clear();
        txtUserName.clear();
    }

    public void newReservationOnAction(ActionEvent actionEvent) {
        clear();
        setReservationId();
        btn.setText("Save Reservation");
    }

    private void setUI(String location) throws IOException {
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        stage.centerOnScreen();
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUI("DashboardForm");
    }

    private boolean saveReservation(Reservation reservation) throws ClassNotFoundException, SQLException {
        Connection connection= DbConnection.getInstance().getConnection();
        PreparedStatement statement=connection.prepareStatement("INSERT INTO reservation values (?,?,?,?,?,?)");
        statement.setString(1,reservation.getId());
        statement.setString(2,reservation.getUserName());
        statement.setString(3,reservation.getRoomType());
        statement.setObject(4,reservation.getCheckInDate());
        statement.setObject(5,reservation.getCheckOutDate());
        statement.setString(6,reservation.getTotalCost());


        int rowCount = statement.executeUpdate();
        return rowCount>0;
    }

    private String getLastId() throws ClassNotFoundException, SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "SELECT id FROM reservation " +  // Base query to select id
                "ORDER BY CAST(SUBSTRING(id, 3) AS UNSIGNED) DESC LIMIT 1";
        PreparedStatement statement = connection.prepareStatement(sql);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }

    private List<Reservation> searchReservations(String text) throws ClassNotFoundException, SQLException {

        text="%"+text+"%";
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM reservation WHERE userName LIKE ?");
        statement.setString(1,text);
        ResultSet resultSet = statement.executeQuery();
        List<Reservation> list=new ArrayList<>();
        while (resultSet.next()){
            list.add(
                    new Reservation(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getDate(4),
                            resultSet.getDate(5),
                            resultSet.getString(6)
                    )
            );
        }
        return list;
    }

    private boolean deleteReservation(String id) throws ClassNotFoundException, SQLException {

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement("DELETE FROM reservation WHERE id=?");
        statement.setString(1,id);

        return statement.executeUpdate()>0;

    }

    private boolean updateReservation(Reservation reservation) throws ClassNotFoundException, SQLException {

        Connection connection= DbConnection.getInstance().getConnection();
        PreparedStatement statement=connection.prepareStatement("UPDATE reservation SET userName=?, roomType=?, checkInDate=?, checkOutDate=?, totalCost=? WHERE id=?");
        statement.setObject(3,reservation.getCheckInDate());
        statement.setObject(4,reservation.getCheckOutDate());
        statement.setString(2,reservation.getRoomType());
        statement.setString(5,reservation.getTotalCost());
        statement.setString(1,reservation.getUserName());
        statement.setString(6,reservation.getId());

        int rowCount = statement.executeUpdate();
        return rowCount>0;
    }

}
