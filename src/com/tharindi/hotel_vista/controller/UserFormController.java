package com.tharindi.hotel_vista.controller;

import com.tharindi.hotel_vista.db.DbConnection;
import com.tharindi.hotel_vista.model.User;
import com.tharindi.hotel_vista.view.tm.UserTm;
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

public class UserFormController {
    public AnchorPane context;
    public TextField txtId;
    public TextField txtName;
    public TextField txtAddress;
    public TextField txtPhone;
    public TableView<UserTm> tblUsers;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colPhone;
    public TableColumn colAddress;
    public TableColumn colOption;
    public Button btn;
    public TextField txtSearch;

    String searchText="";


    public void initialize(){
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));
        setUserId();
        setTableData(searchText);

        txtSearch.textProperty().addListener((observable,oldValue,newValue)->{
            searchText=newValue;
            setTableData(searchText);
        });

        tblUsers.getSelectionModel().selectedItemProperty().addListener((observable,oldValue,newValue)->{
            if (null!=newValue){
                setData(newValue);
            }

        });
    }

    private void setData(UserTm tm) {
        txtId.setText(tm.getId());
        txtName.setText(tm.getName());
        txtPhone.setText(tm.getPhone());
        txtAddress.setText(tm.getAddress());

        btn.setText("Update User");
    }

    private void setTableData(String searchText) {
        ObservableList<UserTm> obList= FXCollections.observableArrayList();

        try {
            for (User user:searchUsers(searchText)){
                    Button button = new Button("delete");
                    UserTm userTm=new UserTm(
                            user.getId(), user.getName(), user.getPhone(), user.getAddress(),button
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
                                if (deleteUser(user.getId())){
                                    new Alert(Alert.AlertType.INFORMATION,"Deleted...!");
                                    setTableData(searchText);
                                    clear();
                                    setUserId();
                                    btn.setText("Save User");
                                }else {
                                    new Alert(Alert.AlertType.WARNING,"Try Again!...").show();
                                }
                            }  catch (ClassNotFoundException | SQLException e) {
                                new Alert(Alert.AlertType.ERROR,e.toString()).show();
                            }

                        }

                    });

                    obList.add(userTm);

            }
            tblUsers.setItems(obList);

        } catch (ClassNotFoundException | SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.toString()).show();
            System.out.println(e.toString());
        }
    }

    private void setUserId() {


        try {
            String lastId = getLastId();
            if (null!=lastId){
                String splitData[] =lastId.split("-");
                String lastIdIntegerNumberAsString=splitData[1];
                int lastIdIntegerAsInt=Integer.parseInt(lastIdIntegerNumberAsString);
                lastIdIntegerAsInt++;
                String generatedUserId=String.format("U-%03d", lastIdIntegerAsInt);
                txtId.setText(generatedUserId);
            }else {
                txtId.setText("U-001");
            }
        } catch (ClassNotFoundException | SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.toString()).show();
            System.out.println(e.toString());
        }

    }

    public void saveOnAction(ActionEvent actionEvent) {

        if (!validatePhoneNumber()) {
            return;
        }

        User user=new User(
                txtId.getText(),
                txtName.getText(),
                txtPhone.getText(),
                txtAddress.getText()
        );

        if (btn.getText().equalsIgnoreCase("Save User")){
            try {
                if (saveUser(user)){
                    System.out.println(user.toString());
                    setUserId();
                    clear();
                    setTableData(searchText);
                    new Alert(Alert.AlertType.INFORMATION,"User Saved!...").show();
                }else {
                    new Alert(Alert.AlertType.WARNING,"Try Again!...").show();
                }

            }  catch (ClassNotFoundException | SQLException e) {
                new Alert(Alert.AlertType.ERROR,e.toString()).show();
            }
        }else {

            try {
                if (updateUser(user)){
                    System.out.println(user.toString());
                    setUserId();
                    clear();
                    setTableData(searchText);
                    btn.setText("Save User");
                    new Alert(Alert.AlertType.INFORMATION,"User Saved!...").show();
                }else {
                    new Alert(Alert.AlertType.WARNING,"Try Again!...").show();
                }

            }  catch (ClassNotFoundException | SQLException e) {
                new Alert(Alert.AlertType.ERROR,e.toString()).show();
            }
        }


    }

    private void clear(){
        txtName.clear();
        txtPhone.clear();
        txtAddress.clear();
    }

    private boolean validatePhoneNumber() {
        String phone = txtPhone.getText();

        if (phone.matches("\\d{10}")) {
            return true;
        } else {
            new Alert(Alert.AlertType.WARNING, "Invalid phone number. It should be exactly 10 digits.").show();
            return false;
        }
    }


    public void newUserOnAction(ActionEvent actionEvent) {
        clear();
        setUserId();
        btn.setText("Save User");
    }

    private void setUI(String location) throws IOException {
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        stage.centerOnScreen();
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUI("DashboardForm");
    }

    private boolean saveUser(User user) throws ClassNotFoundException, SQLException {
        Connection connection= DbConnection.getInstance().getConnection();
        String sql="INSERT INTO user values (?,?,?,?)";
        PreparedStatement statement=connection.prepareStatement(sql);
        statement.setString(1,user.getId());
        statement.setString(2,user.getName());
        statement.setString(3,user.getPhone());
        statement.setString(4,user.getAddress());

        int rowCount = statement.executeUpdate();
        return rowCount>0;
    }

    private String getLastId() throws ClassNotFoundException, SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "SELECT id FROM user  " +  // Base query to select id
                "ORDER BY CAST(SUBSTRING(id, 3) AS UNSIGNED) DESC LIMIT 1";
        PreparedStatement statement = connection.prepareStatement(sql);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }

    private List<User> searchUsers(String text) throws ClassNotFoundException, SQLException {

        text="%"+text+"%";
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM user WHERE name LIKE ? OR address LIKE ?");
        statement.setString(1,text);
        statement.setString(2,text);
        ResultSet resultSet = statement.executeQuery();
        List<User> list=new ArrayList<>();
        while (resultSet.next()){
            list.add(
                    new User(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4)
                    )
            );
        }
        return list;
    }

    private boolean deleteUser(String id) throws ClassNotFoundException, SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement("DELETE FROM user WHERE id=?");
        statement.setString(1,id);

         return statement.executeUpdate()>0;

    }

    private boolean updateUser(User user) throws ClassNotFoundException, SQLException {
        Connection connection= DbConnection.getInstance().getConnection();
        PreparedStatement statement=connection.prepareStatement("UPDATE user SET name=?, phone=?, address=? WHERE id=?");
        statement.setString(1,user.getName());
        statement.setString(2,user.getPhone());
        statement.setString(3,user.getAddress());
        statement.setString(4,user.getId());

        int rowCount = statement.executeUpdate();
        return rowCount>0;
    }

}
