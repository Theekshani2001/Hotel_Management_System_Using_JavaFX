package com.tharindi.hotel_vista.controller;

import com.tharindi.hotel_vista.db.DbConnection;
import com.tharindi.hotel_vista.model.Payroll;
import com.tharindi.hotel_vista.model.Room;
import com.tharindi.hotel_vista.model.User;
import com.tharindi.hotel_vista.view.tm.PayrollTm;
import com.tharindi.hotel_vista.view.tm.RoomTm;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PayrollFormController {
    public AnchorPane context;
    public TextField txtId;
    public TextField txtEmployeeName;
    public TextField txtNetPay;
    public TextField txtSearch;
    public Button btn;
    public TableView tblPayrolls;
    public TableColumn colId;
    public TableColumn colEmployeeName;
    public TableColumn colNetPay;
    public TableColumn colOption;
    public TableColumn colEmployeeType;
    public ComboBox comboEmployeeType;

    String searchText="";


    ObservableList<String> listEmployeeType= FXCollections.observableArrayList("Part Time","Full Time");

    public void initialize(){
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEmployeeName.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        colNetPay.setCellValueFactory(new PropertyValueFactory<>("netPay"));
        colEmployeeType.setCellValueFactory(new PropertyValueFactory<>("employeeType"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("button"));
        comboEmployeeType.setItems(listEmployeeType);
        setPayrollId();
        setTableData(searchText);

        txtSearch.textProperty().addListener((observable,oldValue,newValue)->{
            searchText=newValue;
            setTableData(searchText);
        });

        tblPayrolls.getSelectionModel().selectedItemProperty().addListener((observable,oldValue,newValue)->{
            if (null!=newValue){
                setData((PayrollTm)newValue);
            }

        });
    }

    private void setPayrollId() {
        try {
            String lastId = getLastId();
            if (null!=lastId){
                String splitData[] =lastId.split("-");
                String lastIdIntegerNumberAsString=splitData[1];
                int lastIdIntegerAsInt=Integer.parseInt(lastIdIntegerNumberAsString);
                lastIdIntegerAsInt++;
                String generatedUserId="P-"+lastIdIntegerAsInt;
                txtId.setText(generatedUserId);
            }else {
                txtId.setText("P-1");
            }
        } catch (ClassNotFoundException | SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.toString()).show();
            System.out.println(e.toString());
        }

    }

    private void setData(PayrollTm tm) {
        txtId.setText(tm.getId());
        txtEmployeeName.setText(tm.getEmployeeName());
        txtNetPay.setText(tm.getNetPay());
        comboEmployeeType.setValue(tm.getEmployeeType());

        btn.setText("Update Payroll");
    }

    private void setTableData(String searchText) {
        ObservableList<PayrollTm> obList= FXCollections.observableArrayList();
        try {
            for (Payroll payroll:searchPayrolls(searchText)){
                Button button = new Button("delete");
                PayrollTm payrollTm=new PayrollTm(
                        payroll.getId(),
                        payroll.getEmployeeName(),
                        payroll.getNetPay(),
                        payroll.getEmployeeType(),
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
                            if (deletePayroll(payroll.getId())){
                                new Alert(Alert.AlertType.INFORMATION,"Deleted...!");
                                setTableData(searchText);
                                clear();
                                setPayrollId();
                                btn.setText("Save Payroll");
                            }else {
                                new Alert(Alert.AlertType.WARNING,"Try Again!...").show();
                            }
                        }  catch (ClassNotFoundException | SQLException e) {
                            new Alert(Alert.AlertType.ERROR,e.toString()).show();
                        }

                    }

                });

                obList.add(payrollTm);

            }
            tblPayrolls.setItems(obList);

        } catch (ClassNotFoundException | SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.toString()).show();
            System.out.println(e.toString());
        }
    }

    public void saveOnAction(ActionEvent actionEvent) {

        Payroll payroll=new Payroll(
                txtId.getText(),
                txtEmployeeName.getText(),
                txtNetPay.getText(),
                comboEmployeeType.getValue().toString()
        );

        if (btn.getText().equalsIgnoreCase("Save Payroll")){
            try {
                if (savePayroll(payroll)){
                    System.out.println(payroll.toString());
                    setPayrollId();
                    clear();
                    setTableData(searchText);
                    new Alert(Alert.AlertType.INFORMATION,"Payroll Saved!...").show();
                }else {
                    new Alert(Alert.AlertType.WARNING,"Try Again!...").show();
                }

            }  catch (ClassNotFoundException | SQLException e) {
                new Alert(Alert.AlertType.ERROR,e.toString()).show();
            }
        }else {

            try {
                if (updatePayroll(payroll)){
                    System.out.println(payroll.toString());
                    setPayrollId();
                    clear();
                    setTableData(searchText);
                    btn.setText("Save Payroll");
                    new Alert(Alert.AlertType.INFORMATION,"Payroll Updated!...").show();
                }else {
                    new Alert(Alert.AlertType.WARNING,"Try Again!...").show();
                }

            }  catch (ClassNotFoundException | SQLException e) {
                new Alert(Alert.AlertType.ERROR,e.toString()).show();
            }
        }

    }

    private void clear(){
        txtEmployeeName.clear();
        txtNetPay.clear();
        comboEmployeeType.setValue(null);
    }

    public void newPayrollOnAction(ActionEvent actionEvent) {
        clear();
        setPayrollId();
        btn.setText("Save Payroll");
    }

    private void setUI(String location) throws IOException {
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        stage.centerOnScreen();
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUI("DashboardForm");
    }

    private boolean savePayroll(Payroll payroll) throws ClassNotFoundException, SQLException {
        Connection connection= DbConnection.getInstance().getConnection();
        String sql="INSERT INTO payroll values (?,?,?,?)";
        PreparedStatement statement=connection.prepareStatement(sql);
        statement.setString(1,payroll.getId());
        statement.setString(2,payroll.getEmployeeName());
        statement.setString(3,payroll.getNetPay());
        statement.setString(4,payroll.getEmployeeType());

        int rowCount = statement.executeUpdate();
        return rowCount>0;
    }

    private String getLastId() throws ClassNotFoundException, SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "SELECT id FROM payroll  " +  // Base query to select id
                "ORDER BY CAST(SUBSTRING(id, 3) AS UNSIGNED) DESC LIMIT 1";
        PreparedStatement statement = connection.prepareStatement(sql);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }

    private List<Payroll> searchPayrolls(String text) throws ClassNotFoundException, SQLException {

        text="%"+text+"%";
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM payroll WHERE employeeName LIKE ? ");
        statement.setString(1,text);
        ResultSet resultSet = statement.executeQuery();
        List<Payroll> list=new ArrayList<>();
        while (resultSet.next()){
            list.add(
                    new Payroll(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4)
                    )
            );
        }
        return list;
    }

    private boolean deletePayroll(String id) throws ClassNotFoundException, SQLException {

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement("DELETE FROM payroll WHERE id=?");
        statement.setString(1,id);

        return statement.executeUpdate()>0;

    }

    private boolean updatePayroll(Payroll payroll) throws ClassNotFoundException, SQLException {

        Connection connection= DbConnection.getInstance().getConnection();
        PreparedStatement statement=connection.prepareStatement("UPDATE payroll SET employeeName=?, netPay=?, employeeType=? WHERE id=?");
        statement.setString(1,payroll.getEmployeeName());
        statement.setString(2,payroll.getNetPay());
        statement.setString(3,payroll.getEmployeeType());
        statement.setString(4,payroll.getId());

        int rowCount = statement.executeUpdate();
        return rowCount>0;
    }

}
