package com.tharindi.hotel_vista.controller;

import com.tharindi.hotel_vista.db.DbConnection;
import com.tharindi.hotel_vista.model.BuffetTicket;
import com.tharindi.hotel_vista.model.Room;
import com.tharindi.hotel_vista.view.tm.BuffetTicketTm;
import com.tharindi.hotel_vista.view.tm.RoomTm;
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

public class BuffetTicketFormController {
    public AnchorPane context;
    public TextField txtId;
    public TextField txtUserName;
    public TextField txtPrice;
    public TextField txtSearch;
    public Button btn;
    public TableView tblBuffetTickets;
    public TableColumn colId;
    public TableColumn colUserName;
    public TableColumn colIssueDate;
    public TableColumn colTicketType;
    public TableColumn colPrice;
    public TableColumn colOption;
    public ComboBox comboType;
    public DatePicker txtIssueDate;

    String searchText="";

    ObservableList<String> listTicketTypes= FXCollections.observableArrayList("Adult","Child");


    public void initialize(){
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colIssueDate.setCellValueFactory(new PropertyValueFactory<>("issueDate"));
        colTicketType.setCellValueFactory(new PropertyValueFactory<>("ticketType"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));
        comboType.setItems(listTicketTypes);
        setBuffetTicketId();
        setTableData(searchText);

        txtSearch.textProperty().addListener((observable,oldValue,newValue)->{
            searchText=newValue;
            setTableData(searchText);
        });

        tblBuffetTickets.getSelectionModel().selectedItemProperty().addListener((observable,oldValue,newValue)->{
            if (null!=newValue){
                setData((BuffetTicketTm)newValue);
            }

        });
    }

    private void setBuffetTicketId() {
        try {
            String lastId = getLastId();
            if (null!=lastId){
                String splitData[] =lastId.split("-");
                String lastIdIntegerNumberAsString=splitData[1];
                int lastIdIntegerAsInt=Integer.parseInt(lastIdIntegerNumberAsString);
                lastIdIntegerAsInt++;
                String generatedUserId="B-"+lastIdIntegerAsInt;
                txtId.setText(generatedUserId);
            }else {
                txtId.setText("B-1");
            }
        } catch (ClassNotFoundException | SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.toString()).show();
            System.out.println(e.toString());
        }

    }

    private void setData(BuffetTicketTm tm) {
        txtId.setText(tm.getId());
        txtUserName.setText(tm.getUserName());
        txtIssueDate.setValue(LocalDate.parse(tm.getIssueDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        comboType.setValue(tm.getTicketType());
        txtPrice.setText(tm.getPrice());


        btn.setText("Update Buffet Ticket");
    }

    private void setTableData(String searchText) {
        ObservableList<BuffetTicketTm> obList= FXCollections.observableArrayList();
        try {
            for (BuffetTicket buffetTicket:searchBuffetTickets(searchText)){
                Button button = new Button("delete");
                BuffetTicketTm buffetTicketTm=new BuffetTicketTm(
                        buffetTicket.getId(),
                        new SimpleDateFormat("yyyy-MM-dd").format(buffetTicket.getIssueDate()) ,
                        buffetTicket.getTicketType(),
                        buffetTicket.getPrice(),
                        buffetTicket.getUserName(),
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
                            if (deleteBuffetTicket(buffetTicket.getId())){
                                new Alert(Alert.AlertType.INFORMATION,"Deleted...!");
                                setTableData(searchText);
                                clear();
                                setBuffetTicketId();
                                btn.setText("Save Buffet Ticket");
                            }else {
                                new Alert(Alert.AlertType.WARNING,"Try Again!...").show();
                            }
                        }  catch (ClassNotFoundException | SQLException e) {
                            new Alert(Alert.AlertType.ERROR,e.toString()).show();
                        }

                    }

                });

                obList.add(buffetTicketTm);

            }
            tblBuffetTickets.setItems(obList);

        } catch (ClassNotFoundException | SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.toString()).show();
            System.out.println(e.toString());
        }
    }

    public void saveOnAction(ActionEvent actionEvent) {

        if (!validateIssueDate()) {
            return; // Prevent saving if the date validation fails
        }

        BuffetTicket buffetTicket=new BuffetTicket(
                txtId.getText(),
                Date.from(txtIssueDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                comboType.getValue().toString(),
                txtPrice.getText(),
                txtUserName.getText()
        );

        if (btn.getText().equalsIgnoreCase("Save Buffet Ticket")){
            try {
                if (saveBuffetTicket(buffetTicket)){
                    System.out.println(buffetTicket.toString());
                    setBuffetTicketId();
                    clear();
                    setTableData(searchText);
                    new Alert(Alert.AlertType.INFORMATION,"Buffet Ticket Saved!...").show();
                }else {
                    new Alert(Alert.AlertType.WARNING,"Try Again!...").show();
                }

            }  catch (ClassNotFoundException | SQLException e) {
                new Alert(Alert.AlertType.ERROR,e.toString()).show();
            }
        }else {

            try {
                if (updateBuffetTicket(buffetTicket)){
                    System.out.println(buffetTicket.toString());
                    setBuffetTicketId();
                    clear();
                    setTableData(searchText);
                    btn.setText("Save Buffet Ticket");
                    new Alert(Alert.AlertType.INFORMATION,"Buffet Ticket Updated!...").show();
                }else {
                    new Alert(Alert.AlertType.WARNING,"Try Again!...").show();
                }

            }  catch (ClassNotFoundException | SQLException e) {
                new Alert(Alert.AlertType.ERROR,e.toString()).show();
            }
        }

    }

    private boolean validateIssueDate() {
        LocalDate selectedDate = txtIssueDate.getValue();
        LocalDate today = LocalDate.now();

        if (selectedDate == null) {
            new Alert(Alert.AlertType.WARNING, "Please select an issue date!").show();
            return false;
        } else if (selectedDate.isBefore(today)) {
            new Alert(Alert.AlertType.WARNING, "Issue date cannot be a past date!").show();
            return false;
        }
        return true;
    }

    private void clear(){

        txtIssueDate.setValue(null);
        comboType.setValue(null);
        txtPrice.clear();
        txtUserName.clear();
    }

    public void newBuffetTicketOnAction(ActionEvent actionEvent) {
        clear();
        setBuffetTicketId();
        btn.setText("Save Buffet Ticket");
    }

    private void setUI(String location) throws IOException {
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        stage.centerOnScreen();
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUI("DashboardForm");
    }

    private boolean saveBuffetTicket(BuffetTicket buffetTicket) throws ClassNotFoundException, SQLException {
        Connection connection= DbConnection.getInstance().getConnection();
        String sql="INSERT INTO buffetTicket values (?,?,?,?,?)";
        PreparedStatement statement=connection.prepareStatement(sql);
        statement.setString(1,buffetTicket.getId());
        statement.setObject(2,buffetTicket.getIssueDate());
        statement.setString(3,buffetTicket.getTicketType());
        statement.setString(4,buffetTicket.getPrice());
        statement.setString(5,buffetTicket.getUserName());

        int rowCount = statement.executeUpdate();
        return rowCount>0;
    }

    private String getLastId() throws ClassNotFoundException, SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "SELECT id FROM buffetTicket  " +  // Base query to select id
                "ORDER BY CAST(SUBSTRING(id, 3) AS UNSIGNED) DESC LIMIT 1";
        PreparedStatement statement = connection.prepareStatement(sql);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }

    private List<BuffetTicket> searchBuffetTickets(String text) throws ClassNotFoundException, SQLException {

        text="%"+text+"%";
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM buffetTicket WHERE userName LIKE ?");
        statement.setString(1,text);
        ResultSet resultSet = statement.executeQuery();
        List<BuffetTicket> list=new ArrayList<>();
        while (resultSet.next()){
            list.add(
                    new BuffetTicket(
                            resultSet.getString(1),
                            resultSet.getDate(2),
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getString(5)
                    )
            );
        }
        return list;
    }

    private boolean deleteBuffetTicket(String id) throws ClassNotFoundException, SQLException {

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement("DELETE FROM buffetTicket WHERE id=?");
        statement.setString(1,id);

        return statement.executeUpdate()>0;

    }

    private boolean updateBuffetTicket(BuffetTicket buffetTicket) throws ClassNotFoundException, SQLException {

        Connection connection= DbConnection.getInstance().getConnection();
        PreparedStatement statement=connection.prepareStatement("UPDATE buffetTicket SET issueDate=?, ticketType=?, price=?, userName=? WHERE id=?");
        statement.setObject(1,buffetTicket.getIssueDate());
        statement.setString(2,buffetTicket.getTicketType());
        statement.setString(3,buffetTicket.getPrice());
        statement.setString(4,buffetTicket.getUserName());
        statement.setString(5,buffetTicket.getId());

        int rowCount = statement.executeUpdate();
        return rowCount>0;
    }





}
