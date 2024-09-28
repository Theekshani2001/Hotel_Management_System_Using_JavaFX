package com.tharindi.hotel_vista.controller;

import com.tharindi.hotel_vista.db.DbConnection;
import com.tharindi.hotel_vista.model.Room;
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
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomFormController {
    public TextField txtId;
    public TextField txtFloorNumber;
    public TextField txtSearch;
    public TableColumn colId;
    public Button btn;
    public TableColumn colType;
    public TableColumn colStatus;
    public TableColumn colFloorNumber;
    public TableColumn colOption;
    public ComboBox comboType;
    public ComboBox comboStatus;
    public AnchorPane context;
    public TableView tblRooms;
    String searchText="";

    ObservableList<String> listRoomTypes= FXCollections.observableArrayList("Single Room","Double Room","King Size Room","Suit Room");
    ObservableList<String> listRoomStatus= FXCollections.observableArrayList("Available","Occupied", "Dirty", "OutOfService");

    public void initialize(){
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colType.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("roomStatus"));
        colFloorNumber.setCellValueFactory(new PropertyValueFactory<>("floorNumber"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));
        comboType.setItems(listRoomTypes);
        comboStatus.setItems(listRoomStatus);
        setRoomId();
        setTableData(searchText);

        txtSearch.textProperty().addListener((observable,oldValue,newValue)->{
            searchText=newValue;
            setTableData(searchText);
        });

        tblRooms.getSelectionModel().selectedItemProperty().addListener((observable,oldValue,newValue)->{
            if (null!=newValue){
                setData((RoomTm)newValue);
            }

        });
    }

    private void setRoomId() {
        try {
            String lastId = getLastId();
            if (null!=lastId){
                String splitData[] =lastId.split("-");
                String lastIdIntegerNumberAsString=splitData[1];
                int lastIdIntegerAsInt=Integer.parseInt(lastIdIntegerNumberAsString);
                lastIdIntegerAsInt++;
                String generatedUserId= String.format("R-%03d", lastIdIntegerAsInt);
                txtId.setText(generatedUserId);
            }else {
                txtId.setText("R-001");
            }
        } catch (ClassNotFoundException | SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.toString()).show();
            System.out.println(e.toString());
        }

    }

    private void setData(RoomTm tm) {
        txtId.setText(tm.getId());
        comboType.setValue(tm.getRoomType());
        comboStatus.setValue(tm.getRoomStatus());
        txtFloorNumber.setText(tm.getFloorNumber());

        btn.setText("Update Room");
    }

    private void setTableData(String searchText) {
        ObservableList<RoomTm> obList= FXCollections.observableArrayList();
        try {
            for (Room room:searchRooms(searchText)){
                Button button = new Button("delete");
                RoomTm roomTm=new RoomTm(
                        room.getId(),room.getRoomType(),room.getRoomStatus(),room.getFloorNumber(),button
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
                            if (deleteRoom(room.getId())){
                                new Alert(Alert.AlertType.INFORMATION,"Deleted...!");
                                setTableData(searchText);
                                clear();
                                setRoomId();
                                btn.setText("Save Room");
                            }else {
                                new Alert(Alert.AlertType.WARNING,"Try Again!...").show();
                            }
                        }  catch (ClassNotFoundException | SQLException e) {
                            new Alert(Alert.AlertType.ERROR,e.toString()).show();
                        }

                    }

                });

                obList.add(roomTm);

            }
            tblRooms.setItems(obList);

        } catch (ClassNotFoundException | SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.toString()).show();
            System.out.println(e.toString());
        }
    }

    public void saveOnAction(ActionEvent actionEvent) {

        Room room=new Room(
                txtId.getText(),
                comboType.getValue().toString(),
                comboStatus.getValue().toString(),
                txtFloorNumber.getText()
        );

        if (btn.getText().equalsIgnoreCase("Save Room")){
            try {
                if (saveRoom(room)){
                    System.out.println(room.toString());
                    setRoomId();
                    clear();
                    setTableData(searchText);
                    new Alert(Alert.AlertType.INFORMATION,"Room Saved!...").show();
                }else {
                    new Alert(Alert.AlertType.WARNING,"Try Again!...").show();
                }

            }  catch (ClassNotFoundException | SQLException e) {
                new Alert(Alert.AlertType.ERROR,e.toString()).show();
            }
        }else {

            try {
                if (updateRoom(room)){
                    System.out.println(room.toString());
                    setRoomId();
                    clear();
                    setTableData(searchText);
                    btn.setText("Save Room");
                    new Alert(Alert.AlertType.INFORMATION,"Room Updated!...").show();
                }else {
                    new Alert(Alert.AlertType.WARNING,"Try Again!...").show();
                }

            }  catch (ClassNotFoundException | SQLException e) {
                new Alert(Alert.AlertType.ERROR,e.toString()).show();
            }
        }

    }

    private void clear(){
        txtFloorNumber.clear();
    }

    public void newRoomOnAction(ActionEvent actionEvent) {
        clear();
        setRoomId();
        btn.setText("Save Room");
    }

    private void setUI(String location) throws IOException {
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        stage.centerOnScreen();
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUI("DashboardForm");
    }

    private boolean saveRoom(Room room) throws ClassNotFoundException, SQLException {
        Connection connection= DbConnection.getInstance().getConnection();
        String sql="INSERT INTO room values (?,?,?,?)";
        PreparedStatement statement=connection.prepareStatement(sql);
        statement.setString(1,room.getId());
        statement.setString(2,room.getRoomType());
        statement.setString(3,room.getRoomStatus());
        statement.setString(4,room.getFloorNumber());

        int rowCount = statement.executeUpdate();
        return rowCount>0;
    }

    private String getLastId() throws ClassNotFoundException, SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "SELECT id FROM room  " +  // Base query to select id
                "ORDER BY CAST(SUBSTRING(id, 3) AS UNSIGNED) DESC LIMIT 1";
        PreparedStatement statement = connection.prepareStatement(sql);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }

    private List<Room> searchRooms(String text) throws ClassNotFoundException, SQLException {

        text="%"+text+"%";
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM room WHERE roomType LIKE ? OR roomStatus LIKE ?");
        statement.setString(1,text);
        statement.setString(2,text);
        ResultSet resultSet = statement.executeQuery();
        List<Room> list=new ArrayList<>();
        while (resultSet.next()){
            list.add(
                    new Room(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4)
                    )
            );
        }
        return list;
    }

    private boolean deleteRoom(String id) throws ClassNotFoundException, SQLException {

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement("DELETE FROM room WHERE id=?");
        statement.setString(1,id);

        return statement.executeUpdate()>0;

    }

    private boolean updateRoom(Room room) throws ClassNotFoundException, SQLException {

        Connection connection= DbConnection.getInstance().getConnection();
        PreparedStatement statement=connection.prepareStatement("UPDATE room SET roomType=?, roomStatus=?, floorNumber=? WHERE id=?");
        statement.setString(1,room.getRoomType());
        statement.setString(2,room.getRoomStatus());
        statement.setString(3,room.getFloorNumber());
        statement.setString(4,room.getId());

        int rowCount = statement.executeUpdate();
        return rowCount>0;
    }

}
