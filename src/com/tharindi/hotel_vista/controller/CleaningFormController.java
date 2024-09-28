package com.tharindi.hotel_vista.controller;

import com.tharindi.hotel_vista.db.DbConnection;
import com.tharindi.hotel_vista.model.BuffetTicket;
import com.tharindi.hotel_vista.model.Cleaning;
import com.tharindi.hotel_vista.view.tm.BuffetTicketTm;
import com.tharindi.hotel_vista.view.tm.CleaningTm;
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

public class CleaningFormController {
    public AnchorPane context;
    public TextField txtId;
    public TextField txtSearch;
    public Button btn;
    public TableView tblCleanings;
    public TableColumn colId;
    public TableColumn colCleaningDate;
    public TableColumn colCleaningStatus;
    public TableColumn colRoomNumber;
    public DatePicker txtCleaningDate;
    public ComboBox comboStatus;
    public ComboBox comboNumber;


    String searchText="";

    ObservableList<String> listTypes;
    ObservableList<String> listStatus= FXCollections.observableArrayList("Clean", "Dirty", "InProgress");


    public void initialize(){
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCleaningDate.setCellValueFactory(new PropertyValueFactory<>("cleaningDate"));
        colCleaningStatus.setCellValueFactory(new PropertyValueFactory<>("cleaningStatus"));
        colRoomNumber.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        comboStatus.setItems(listStatus);

        try {
            listTypes = getRoomNumbers();  // Fetch room numbers from the database
            comboNumber.setItems(listTypes);  // Set room numbers in the combo box
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading room numbers: " + e.getMessage()).show();
        }



        setCleaningId();
        setTableData(searchText);

        txtSearch.textProperty().addListener((observable,oldValue,newValue)->{
            searchText=newValue;
            setTableData(searchText);
        });

        tblCleanings.getSelectionModel().selectedItemProperty().addListener((observable,oldValue,newValue)->{
            if (null!=newValue){
                setData((CleaningTm)newValue);
            }

        });

        txtCleaningDate.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;");
                }
            }
        });
    }

    private void setCleaningId() {
        try {
            String lastId = getLastId();
            if (null!=lastId){
                String splitData[] =lastId.split("-");
                String lastIdIntegerNumberAsString=splitData[1];
                int lastIdIntegerAsInt=Integer.parseInt(lastIdIntegerNumberAsString);
                lastIdIntegerAsInt++;
                String generatedUserId=String.format("C-%03d", lastIdIntegerAsInt);;
                txtId.setText(generatedUserId);
            }else {
                txtId.setText("C-001");
            }
        } catch (ClassNotFoundException | SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.toString()).show();
            System.out.println(e.toString());
        }

    }

    private ObservableList<String> getRoomNumbers() throws SQLException, ClassNotFoundException {
        ObservableList<String> roomNumbers = FXCollections.observableArrayList();

        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "SELECT id FROM room";  // Assuming 'roomNumber' is the field in the 'room' table
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            roomNumbers.add(resultSet.getString("id"));
        }

        return roomNumbers;
    }


    private void setData(CleaningTm tm) {
        txtId.setText(tm.getId());
        txtCleaningDate.setValue(LocalDate.parse(tm.getCleaningDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        comboStatus.setValue(tm.getCleaningStatus());
        comboNumber.setValue(tm.getRoomNumber());
        btn.setText("Update Cleaning Task");
    }

    private void setTableData(String searchText) {
        ObservableList<CleaningTm> obList= FXCollections.observableArrayList();
        try {
            for (Cleaning cleaning:searchCleanings(searchText)){
                Button button = new Button("delete");
                CleaningTm cleaningTm=new CleaningTm(
                        cleaning.getId(),
                        new SimpleDateFormat("yyyy-MM-dd").format(cleaning.getCleaningDate()) ,
                        cleaning.getCleaningStatus(),
                        cleaning.getRoomNumber(),
                        button
                );
                //System.out.println(cleaningTm.toString());

                button.setOnAction(event -> {
                    Alert alert=new Alert(
                            Alert.AlertType.CONFIRMATION,"Are you Sure..?",
                            ButtonType.YES,
                            ButtonType.NO
                    );
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.get().equals(ButtonType.YES)){

                        try {
                            if (deleteCleaning(cleaning.getId())){
                                new Alert(Alert.AlertType.INFORMATION,"Deleted...!");
                                setTableData(searchText);
                                clear();
                                setCleaningId();
                                btn.setText("Save Cleaning Task");
                            }else {
                                new Alert(Alert.AlertType.WARNING,"Try Again!...").show();
                            }
                        }  catch (ClassNotFoundException | SQLException e) {
                            new Alert(Alert.AlertType.ERROR,e.toString()).show();
                        }

                    }

                });

                obList.add(cleaningTm);

            }
            tblCleanings.setItems(obList);

        } catch (ClassNotFoundException | SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.toString()).show();
            System.out.println(e.toString());
        }
    }

    public void saveOnAction(ActionEvent actionEvent) {

        Cleaning cleaning=new Cleaning(
                txtId.getText(),
                Date.from(txtCleaningDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                comboStatus.getValue().toString(),
                comboNumber.getValue().toString()
        );

        if (btn.getText().equalsIgnoreCase("Save Cleaning Task")){
            try {
                if (saveCleaning(cleaning)){
                    System.out.println(cleaning.toString());
                    setCleaningId();
                    clear();
                    setTableData(searchText);
                    new Alert(Alert.AlertType.INFORMATION,"Cleaning Task Saved!...").show();
                }else {
                    new Alert(Alert.AlertType.WARNING,"Try Again!...").show();
                }

            }  catch (ClassNotFoundException | SQLException e) {
                new Alert(Alert.AlertType.ERROR,e.toString()).show();
            }
        }else {

            try {
                if (updateCleaning(cleaning)){
                    System.out.println(cleaning.toString());
                    setCleaningId();
                    clear();
                    setTableData(searchText);
                    btn.setText("Save Cleaning Task");
                    new Alert(Alert.AlertType.INFORMATION,"Cleaning Task Updated!...").show();
                }else {
                    new Alert(Alert.AlertType.WARNING,"Try Again!...").show();
                }

            }  catch (ClassNotFoundException | SQLException e) {
                new Alert(Alert.AlertType.ERROR,e.toString()).show();
            }
        }

    }

    private void clear(){

        txtCleaningDate.setValue(null);
        //comboType.setValue(null);
        comboStatus.setValue(null);
    }

    public void newCleaningOnAction(ActionEvent actionEvent) {
        clear();
        setCleaningId();
        btn.setText("Save Cleaning Task");
    }

    private void setUI(String location) throws IOException {
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        stage.centerOnScreen();
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUI("DashboardForm");
    }

    private boolean saveCleaning(Cleaning cleaning) throws ClassNotFoundException, SQLException {
        Connection connection= DbConnection.getInstance().getConnection();
        String sql="INSERT INTO cleaning values (?,?,?,?)";
        PreparedStatement statement=connection.prepareStatement(sql);
        statement.setString(1,cleaning.getId());
        statement.setObject(2,cleaning.getCleaningDate());
        statement.setString(3,cleaning.getCleaningStatus());
        statement.setString(4,cleaning.getRoomNumber());

        int rowCount = statement.executeUpdate();
        return rowCount>0;
    }

    private String getLastId() throws ClassNotFoundException, SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "SELECT id FROM cleaning  " +  // Base query to select id
                "ORDER BY CAST(SUBSTRING(id, 3) AS UNSIGNED) DESC LIMIT 1";
        PreparedStatement statement = connection.prepareStatement(sql);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }

    private List<Cleaning> searchCleanings(String text) throws ClassNotFoundException, SQLException {

        text="%"+text+"%";
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM cleaning WHERE cleaningStatus LIKE ? OR roomNumber LIKE ?");
        statement.setString(1,text);
        statement.setString(2,text);
        ResultSet resultSet = statement.executeQuery();
        List<Cleaning> list=new ArrayList<>();
        while (resultSet.next()){
            list.add(
                    new Cleaning(
                            resultSet.getString(1),
                            resultSet.getDate(2),
                            resultSet.getString(3),
                            resultSet.getString(4)
                    )
            );
        }
        return list;
    }

    private boolean deleteCleaning(String id) throws ClassNotFoundException, SQLException {

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement("DELETE FROM cleaning WHERE id=?");
        statement.setString(1,id);

        return statement.executeUpdate()>0;

    }

    private boolean updateCleaning(Cleaning cleaning) throws ClassNotFoundException, SQLException {

        Connection connection= DbConnection.getInstance().getConnection();
        PreparedStatement statement=connection.prepareStatement("UPDATE cleaning SET cleaningDate=?, cleaningStatus=?, roomNumber=? WHERE id=?");
        statement.setObject(1,cleaning.getCleaningDate());
        statement.setString(2,cleaning.getCleaningStatus());
        statement.setString(3,cleaning.getRoomNumber());
        statement.setString(4,cleaning.getId());

        int rowCount = statement.executeUpdate();
        return rowCount>0;
    }

}
