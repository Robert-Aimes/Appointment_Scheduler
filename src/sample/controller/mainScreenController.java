package sample.controller;

import com.sun.javafx.stage.EmbeddedWindow;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.DAO.JDBC;
import sample.main.Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.fxml.Initializable;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.ZoneId;
import java.util.ResourceBundle;
import java.util.Locale;
import java.util.TimeZone;




public class mainScreenController implements Initializable{

    @FXML private RadioButton appointmentByWeekRadio;
    @FXML private RadioButton appointmentByMonthRadio;
    @FXML private RadioButton appointmentAllRadio;
    @FXML private TableView appointmentTable;
    @FXML private TableView customerTable;
    @FXML private TableColumn appointmentIdColumn;
    @FXML private TableColumn appointmentTitleColumn;
    @FXML private TableColumn appointmentDescriptionColumn;
    @FXML private TableColumn appointmentLocationColumn;
    @FXML private TableColumn appointmentStartColumn;
    @FXML private TableColumn appointmentEndColumn;
    @FXML private TableColumn appointmentCustomerIdColumn;
    @FXML private TableColumn appointmentContactColumn;
    @FXML private TableColumn appointmentTypeColumn;
    @FXML private TableColumn appointmentUserIdColumn;
    @FXML private TableColumn customerIdColumn;
    @FXML private TableColumn customerNameColumn;
    @FXML private TableColumn customerAddressColumn;
    @FXML private TableColumn customerPostalColumn;
    @FXML private TableColumn customerPhoneColumn;
    @FXML private TableColumn customerStateColumn;
    @FXML private TableColumn customerCreatedDateColumn;
    @FXML private TableColumn customerCreatedByColumn;
    @FXML private TableColumn customerLastUpdateColumn;
    @FXML private TableColumn customerLastUpdatedByColumn;
    @FXML private Button addApptButton;
    @FXML private Button updateApptButton;
    @FXML private Button deleteApptButton;
    @FXML private Button adjustApptTimeButton;
    @FXML private Button addCustomerButton;
    @FXML private Button modifyCustomerButton;
    @FXML private Button deleteCustomerButton;
    @FXML private Button reportButton;
    @FXML private Button logoutButton;





    public void addApptButtonClicked(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent mainScreenWindow = FXMLLoader.load(getClass().getResource("../view/addAppointment.fxml"));
        Scene mainScreenScene = new Scene(mainScreenWindow);
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(mainScreenScene);
        window.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        appointmentAllRadio.setSelected(true);

    }
}


