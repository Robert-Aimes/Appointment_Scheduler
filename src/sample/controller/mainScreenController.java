package sample.controller;

import com.sun.javafx.stage.EmbeddedWindow;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
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
import sample.DAO.*;
import sample.main.Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.fxml.Initializable;
import sample.model.Appointment;
import sample.model.Contacts;
import sample.model.Countries;
import sample.model.Customer;


import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.*;
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
        try {
            Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
            if (selectedCustomer == null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Please select a Customer to update.", ButtonType.OK);
                alert.showAndWait();
            } else {

                Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/modifyCustomer.fxml"));
                Parent scene = loader.load();
                modifyCustomerController controller = loader.getController();
                controller.setCustomer(selectedCustomer);
                stage.setTitle("Update Customer");
                stage.setScene(new Scene(scene));
                stage.show();

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {

        }
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
                String apptType = selectedAppointment.getApptType();
                AppointmentDb.deleteAppointment(selectedAppointmentId);
                ObservableList<Appointment> allAppointments = AppointmentDb.getAllAppointments();
                appointmentTable.setItems(allAppointments);
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Appointment successfully deleted: Appointment ID = " + selectedAppointmentId + " Appointment Type: " + apptType, ButtonType.OK);
                confirmationAlert.showAndWait();

            }
        }
    }

    /**
     * This method handles the functionality when the delete customer button is clicked. It also calls a different method that checks whether a customer has an appointment as well throwing an error if they do.
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     */
    public void deleteCustomerButtonClicked(ActionEvent actionEvent) throws IOException, SQLException {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

        if (selectedCustomer == null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Please select a Customer from the Customer table.", ButtonType.OK);
            alert.showAndWait();
        } else {
            int customerID = selectedCustomer.getCustId();
            boolean hasAppointments = checkCustomerAppointments(customerID);

            if (hasAppointments) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you would like to delete this Customer?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES) {
                    int selectedCustomerId = selectedCustomer.getCustId();
                    CustomerDb customerDb = new CustomerDb();
                    customerDb.deleteCustomer(selectedCustomerId);
                    ObservableList<Customer> allCustomers = CustomerDb.getAllCustomers();
                    customerTable.setItems(allCustomers);

                    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Customer: " + selectedCustomerId + " successfully deleted.", ButtonType.OK);
                    confirmationAlert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "You cannot delete this customer because they have related appointments.");
                alert.showAndWait();
            }
        }
    }

    /**
     * LAMBDA EXPRESSION created to check if a customer has a related appointment in the appointment table
     * we use the stream() method to convert the appointmentList into a stream. Then, we use the anyMatch method along with a lambda expression to check if there is any appointment with a matching customerId.
     * @param customerID
     * @return
     * @throws SQLException
     */
    public boolean checkCustomerAppointments(int customerID) throws SQLException {
        ObservableList<Appointment> appointmentList = AppointmentDb.getAllAppointments();

        boolean hasRelatedAppointments = appointmentList.stream()
                .anyMatch(appointment -> appointment.getCustomerId() == customerID);

        if (hasRelatedAppointments) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "You must remove all appointments related to this customer before deleting the customer.");
            alert.showAndWait();
            return false;
        }

        return true;
    }

    /**
     * Created method to check if there is an appointment within 15 minutes of the users log in time
     * @param
     * @throws SQLException
     */
    public void checkAppointmentTimes() throws SQLException {
        LocalDateTime logInTime = LocalDateTime.now(ZoneId.systemDefault());
        ObservableList<Appointment> apptList = AppointmentDb.getAllAppointments();
        boolean foundAppointment = false;

        for (Appointment appt : apptList) {
            LocalDateTime apptStartTime = appt.getApptStartTime();
            System.out.println(apptStartTime);
            // Convert appointment start time from UTC to user's local time
            ZoneId userTimeZone = ZoneId.systemDefault();
            ZonedDateTime apptStartLocal = apptStartTime.atZone(ZoneOffset.UTC).withZoneSameInstant(userTimeZone);
            System.out.println(apptStartTime + "compare " + apptStartLocal);
            // Calculate the time difference between the current time and the appointment's start time
            long minutesDifference = Duration.between(logInTime, apptStartLocal).toMinutes();

            // Check if the appointment's start time is within 15 minutes of the current time
            if (minutesDifference >= 0 && minutesDifference <= 15) {
                // Display an alert
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "You have an appointment starting soon. Appointment ID: "
                        + appt.getApptId() + " Start Date and Time: " + apptStartLocal, ButtonType.OK);
                alert.showAndWait();
                foundAppointment = true; // Set the flag to true
                break; // Exit the loop after displaying the alert for the first appointment found
            }
        }

        if (!foundAppointment) {
            // Display an alert indicating no appointments within 15 minutes
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "There are no appointments within 15 minutes.", ButtonType.OK);
            alert.showAndWait();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            ObservableList<Appointment> allAppointments = AppointmentDb.getAllAppointments();

            // Create a cell value factory for the start time column
            //This still isn't working right
            appointmentStartColumn.setCellValueFactory(cellData -> {
                Appointment appointment = cellData.getValue();
                LocalDateTime startDateTime = appointment.getApptStartTime();
                ZoneId userTimeZone = ZoneId.systemDefault();
                ZonedDateTime userZDT = ZonedDateTime.of(startDateTime, userTimeZone);
                ZoneId utcTime = ZoneId.of("UTC");
                ZonedDateTime utcZDT = ZonedDateTime.ofInstant(userZDT.toInstant(),utcTime);
                userZDT = ZonedDateTime.ofInstant(utcZDT.toInstant(), userTimeZone);

                return new SimpleStringProperty(userZDT.toString());
            });

            // Create a cell value factory for the end time column
            appointmentEndColumn.setCellValueFactory(cellData -> {
                Appointment appointment = cellData.getValue();
                LocalDateTime endDateTime = appointment.getApptEndTime();
                ZoneId userTimeZone = ZoneId.systemDefault();
                ZonedDateTime userZDT = ZonedDateTime.of(endDateTime, userTimeZone);
                ZoneId utcTime = ZoneId.of("UTC");
                ZonedDateTime utcZDT = ZonedDateTime.ofInstant(userZDT.toInstant(),utcTime);
                userZDT = ZonedDateTime.ofInstant(utcZDT.toInstant(), userTimeZone);
                return new SimpleStringProperty(userZDT.toString());

            });

            appointmentTable.setItems(FXCollections.observableArrayList(allAppointments));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("apptId"));
        appointmentTitleColumn.setCellValueFactory(new PropertyValueFactory<>("apptTitle"));
        appointmentDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("apptDescription"));
        appointmentLocationColumn.setCellValueFactory(new PropertyValueFactory<>("apptLocation"));
        appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("apptType"));
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


