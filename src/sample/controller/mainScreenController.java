package sample.controller;

import com.sun.javafx.stage.EmbeddedWindow;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.DAO.AppointmentDb;
import sample.DAO.ContactsDb;
import sample.DAO.CustomerDb;
import sample.DAO.JDBC;
import sample.main.Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.fxml.Initializable;
import sample.model.Appointment;
import sample.model.Contacts;
import sample.model.Customer;


import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.ResourceBundle;
import java.util.Locale;
import java.util.TimeZone;
import java.util.function.Predicate;


public class mainScreenController implements Initializable{

    @FXML private RadioButton appointmentByWeekRadio;
    @FXML private RadioButton appointmentByMonthRadio;
    @FXML private RadioButton appointmentAllRadio;
    @FXML private TableView<Appointment> appointmentTable;
    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Appointment, Integer> appointmentIdColumn;
    @FXML private TableColumn<Appointment, String> appointmentTitleColumn;
    @FXML private TableColumn<Appointment, String> appointmentDescriptionColumn;
    @FXML private TableColumn<Appointment, String> appointmentLocationColumn;
    @FXML private TableColumn<Appointment, String> appointmentStartColumn;
    @FXML private TableColumn<Appointment, String> appointmentEndColumn;
    @FXML private TableColumn<Appointment, String> appointmentCustomerIdColumn;
    @FXML private TableColumn<Appointment, String> appointmentContactColumn;
    @FXML private TableColumn<Appointment, String> appointmentTypeColumn;
    @FXML private TableColumn<Appointment, Integer> appointmentUserIdColumn;
    @FXML private TableColumn<Customer, Integer> customerIdColumn;
    @FXML private TableColumn<Customer, String> customerNameColumn;
    @FXML private TableColumn<Customer, String> customerAddressColumn;
    @FXML private TableColumn<Customer, String> customerPostalColumn;
    @FXML private TableColumn<Customer, String> customerPhoneColumn;
    @FXML private TableColumn<Customer, String> customerStateColumn;
    @FXML private TableColumn<Customer, String> customerCreatedDateColumn;
    @FXML private TableColumn<Customer, String> customerCreatedByColumn;
    @FXML private TableColumn<Customer, String> customerLastUpdateColumn;
    @FXML private TableColumn<Customer, String> customerLastUpdatedByColumn;
    @FXML private Button addApptButton;
    @FXML private Button updateApptButton;
    @FXML private Button deleteApptButton;
    @FXML private Button adjustApptTimeButton;
    @FXML private Button addCustomerButton;
    @FXML private Button modifyCustomerButton;
    @FXML private Button deleteCustomerButton;
    @FXML private Button reportButton;
    @FXML private Button logoutButton;

    /**
     * Method to handle the selection of the different radio buttons above the Appointment TableView
     * @param actionEvent
     */
    public void radioButtonAction(ActionEvent actionEvent) throws SQLException {
        if(appointmentByMonthRadio.isSelected()){
            LocalDateTime dateTime = LocalDateTime.now();
            Month currentMonth = dateTime.getMonth();

            Predicate<Appointment> inCurrentMonth = appointment ->
                    appointment.getApptStartTime().getMonth() == currentMonth;

// Apply the Predicate to the appointmentList and set the filtered data to the TableView
            ObservableList<Appointment> monthlyAppointments = AppointmentDb.getAllAppointments();
            appointmentTable.setItems(monthlyAppointments.filtered(inCurrentMonth));
        } else if(appointmentByWeekRadio.isSelected()){
            ObservableList<Appointment> weeklyAppointments = AppointmentDb.getAllAppointments();
            FilteredList<Appointment> filteredAppointments = new FilteredList<>(weeklyAppointments);
            filteredAppointments.setPredicate(appointment -> {
                LocalDateTime startTime = appointment.getApptStartTime();
                LocalDate startDate = startTime.toLocalDate();
                LocalDate currentDate = LocalDate.now();
                WeekFields weekFields = WeekFields.of(Locale.getDefault());
                int currentWeekNumber = currentDate.get(weekFields.weekOfWeekBasedYear());
                int appointmentWeekNumber = startDate.get(weekFields.weekOfWeekBasedYear());
                return appointmentWeekNumber == currentWeekNumber;

            });
            appointmentTable.setItems(filteredAppointments);

        } else if(appointmentAllRadio.isSelected()){
            ObservableList<Appointment> allAppointments = AppointmentDb.getAllAppointments();
            appointmentTable.setItems(allAppointments);
        }
    }
    /**
     * Switches screen to add appointment screen
     * @param actionEvent
     * @throws IOException
     */
    public void addApptButtonClicked(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent mainScreenWindow = FXMLLoader.load(getClass().getResource("../view/addAppointment.fxml"));
        Scene mainScreenScene = new Scene(mainScreenWindow);
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(mainScreenScene);
        window.show();
    }

    /**
     * Switches screen to modify appointment screen
     * @param actionEvent
     * @throws IOException
     */
    public void modifyApptButtonClicked(ActionEvent actionEvent) throws IOException {

        try {
            Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
            if (selectedAppointment == null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Please select an Appointment to update.", ButtonType.OK);
                alert.showAndWait();
            } else {

                Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/modifyAppointment.fxml"));
                Parent scene = loader.load();
                modifyAppointmentController controller = loader.getController();
                controller.setAppointment(selectedAppointment);
                stage.setTitle("Update Appointment");
                stage.setScene(new Scene(scene));
                stage.show();

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {

        }
    }


    /**
     * Switches screen to add customer screen
     * @param actionEvent
     * @throws IOException
     */
    public void addCustButtonClicked(ActionEvent actionEvent) throws IOException {
        Parent mainScreenWindow = FXMLLoader.load(getClass().getResource("../view/addCustomer.fxml"));
        Scene mainScreenScene = new Scene(mainScreenWindow);
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(mainScreenScene);
        window.show();
    }

    /**
     * Switches screen to modify customer screen
     * @param actionEvent
     * @throws IOException
     */
    public void modifyCustButtonClicked(ActionEvent actionEvent) throws IOException{
        Parent mainScreenWindow = FXMLLoader.load(getClass().getResource("../view/modifyCustomer.fxml"));
        Scene mainScreenScene = new Scene(mainScreenWindow);
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(mainScreenScene);
        window.show();
    }

    /**
     * Logs out user and goes back to log in page
     * @param actionEvent
     * @throws IOException
     */
    public void logoutButtonClicked(ActionEvent actionEvent) throws IOException{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you wish to Logout of the application?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if(alert.getResult() == ButtonType.YES){
            Parent mainScreenWindow = FXMLLoader.load(getClass().getResource("../view/loginForm.fxml"));
            Scene mainScreenScene = new Scene(mainScreenWindow);
            Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            window.setScene(mainScreenScene);
            window.show();

        }
    }

    /**
     * Switches screen to reporting screen
     * @param actionEvent
     * @throws IOException
     */
    public void reportsButtonClicked(ActionEvent actionEvent) throws IOException{
        Parent mainScreenWindow = FXMLLoader.load(getClass().getResource("../view/reportsForm.fxml"));
        Scene mainScreenScene = new Scene(mainScreenWindow);
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(mainScreenScene);
        window.show();
    }


    /**
     * Handles functionality to delete an appointment from the database and tableview
     * @param actionEvent
     * @throws IOException
     */
    public void deleteAppointmentButtonClicked(ActionEvent actionEvent) throws SQLException {
        Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Please select an Appointment from the Appointment table.", ButtonType.OK);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you would like to delete this Appointment?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                int selectedAppointmentId = selectedAppointment.getApptId();
                AppointmentDb.deleteAppointment(selectedAppointmentId);
                ObservableList<Appointment> allAppointments = AppointmentDb.getAllAppointments();
                appointmentTable.setItems(allAppointments);

            }
        }
    }

    public void deleteCustomerButtonClicked(ActionEvent actionEvent) throws IOException, SQLException {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Please select a Customer from the Customer table.", ButtonType.OK);
            alert.showAndWait();
        } else {
            int customerID = selectedCustomer.getCustId();
            ObservableList<Appointment> appointmentList = AppointmentDb.getAllAppointments();
            for(Appointment appointment : appointmentList) {
                if (appointment.getCustomerId() == customerID) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "You must remove all Appointments related to this Customer before deleting the Customer?", ButtonType.OK);
                }
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you would like to delete this Customer?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                int selectedCustomerId = selectedCustomer.getCustId();
                CustomerDb customerDb = new CustomerDb();
                customerDb.deleteCustomer(selectedCustomerId);
                ObservableList<Customer> allCustomers = CustomerDb.getAllCustomers();
                customerTable.setItems(allCustomers);

            }
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            appointmentTable.setItems(AppointmentDb.getAllAppointments());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("apptId"));
        appointmentTitleColumn.setCellValueFactory(new PropertyValueFactory<>("apptTitle"));
        appointmentDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("apptDescription"));
        appointmentLocationColumn.setCellValueFactory(new PropertyValueFactory<>("apptLocation"));
        appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("apptType"));
        appointmentStartColumn.setCellValueFactory(new PropertyValueFactory<>("apptStartTime"));
        appointmentEndColumn.setCellValueFactory(new PropertyValueFactory<>("apptEndTime"));
        appointmentCustomerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        appointmentContactColumn.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        appointmentUserIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));

        try {
            customerTable.setItems(CustomerDb.getAllCustomers());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("custId"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("custName"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("custAddress"));
        customerPostalColumn.setCellValueFactory(new PropertyValueFactory<>("custPostalCode"));
        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("custPhone"));
        customerStateColumn.setCellValueFactory(new PropertyValueFactory<>("custDivisionName"));
        customerCreatedDateColumn.setCellValueFactory(new PropertyValueFactory<>("custCreateDate"));
        customerCreatedByColumn.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        customerLastUpdateColumn.setCellValueFactory(new PropertyValueFactory<>("custLastUpdate"));
        customerLastUpdatedByColumn.setCellValueFactory(new PropertyValueFactory<>("lastUpdatedBy"));



        appointmentAllRadio.setSelected(true);


    }


}


