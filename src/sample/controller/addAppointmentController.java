package sample.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.DAO.AppointmentDb;
import sample.DAO.ContactsDb;
import sample.DAO.CustomerDb;
import sample.DAO.UsersDb;
import sample.model.Appointment;
import sample.model.Contacts;
import sample.model.Customer;
import sample.model.Users;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class addAppointmentController {
    @FXML
    private TextField addApptIdField;
    @FXML
    private TextField addApptTypeField;
    @FXML
    private TextField addApptDescriptionField;
    @FXML
    private TextField addApptLocationField;
    @FXML
    private TextField addApptTitleField;
    @FXML
    private DatePicker addApptStartDatePicker;
    @FXML
    private DatePicker addApptEndDatePicker;
    @FXML
    private ComboBox<LocalTime> addApptStartTimeChoice;
    @FXML
    private ComboBox<LocalTime> addApptEndTimeChoice;
    @FXML
    private ComboBox<Integer> addApptCustomerIdChoice;
    @FXML
    private ComboBox<Integer> addApptUserIdChoice;
    @FXML
    private ComboBox<String> addApptContactChoice;
    @FXML
    private Button addApptSaveButton;
    @FXML
    private Button addApptCancelButton;

    /**
     * Method to handle functionality when save button is clicked. Populate Appointment database table with all new appt data.
     * @param actionEvent
     * @throws IOException
     */
    public void saveButtonClicked(ActionEvent actionEvent) {
        try{
            LocalTime apptStartTime = addApptStartTimeChoice.getValue();
            LocalTime apptEndTime = addApptEndTimeChoice.getValue();

            // Get the selected start and end dates from the date pickers
            LocalDate startDate = addApptStartDatePicker.getValue();
            LocalDate endDate = addApptEndDatePicker.getValue();

            // Combine the selected date and time into LocalDateTime objects
            LocalDateTime startDateTime = LocalDateTime.of(startDate, apptStartTime);
            LocalDateTime endDateTime = LocalDateTime.of(endDate, apptEndTime);

            // Convert the LocalDateTime objects to UTC by applying the user's time zone offset
            ZoneId userTimeZone = ZoneId.systemDefault();
            ZoneOffset userOffset = userTimeZone.getRules().getOffset(startDateTime);
            Instant startInstant = startDateTime.toInstant(userOffset);
            Instant endInstant = endDateTime.toInstant(userOffset);

            // Convert the UTC Instant values back to LocalDateTime objects
            LocalDateTime utcStartDateTime = LocalDateTime.ofInstant(startInstant, ZoneOffset.UTC);
            LocalDateTime utcEndDateTime = LocalDateTime.ofInstant(endInstant, ZoneOffset.UTC);

            // Check if the appointment falls within business hours (8:00 a.m. to 10:00 p.m. EST)
            ZoneId estTimeZone = ZoneId.of("America/New_York");
            LocalDateTime estStartDateTime = utcStartDateTime.atZone(ZoneOffset.UTC).withZoneSameInstant(estTimeZone).toLocalDateTime();
            LocalDateTime estEndDateTime = utcEndDateTime.atZone(ZoneOffset.UTC).withZoneSameInstant(estTimeZone).toLocalDateTime();
            LocalTime estBusinessHoursStart = LocalTime.of(8, 0);
            LocalTime estBusinessHoursEnd = LocalTime.of(22, 0);

            if (estStartDateTime.toLocalTime().isBefore(estBusinessHoursStart) || estEndDateTime.toLocalTime().isAfter(estBusinessHoursEnd)) {
                // Display an error message for scheduling outside business hours
                Alert alert = new Alert(Alert.AlertType.ERROR, "Appointments must be scheduled between 8:00 a.m. and 10:00 p.m. EST.", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            //Not sure if this is working
            // Check for overlapping appointments
            List<Appointment> existingAppointments = AppointmentDb.getAllAppointments();
            for (Appointment appointment : existingAppointments) {
                LocalDateTime existingStartDateTime = appointment.getApptStartTime();
                LocalDateTime existingEndDateTime = appointment.getApptEndTime();

                if ((utcStartDateTime.isAfter(existingStartDateTime) && utcStartDateTime.isBefore(existingEndDateTime)) ||
                        (utcEndDateTime.isAfter(existingStartDateTime) && utcEndDateTime.isBefore(existingEndDateTime)) ||
                        (utcStartDateTime.isEqual(existingStartDateTime) || utcEndDateTime.isEqual(existingEndDateTime))) {
                    // Display an error message for overlapping appointments
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Overlapping appointments are not allowed.", ButtonType.OK);
                    alert.showAndWait();
                    return;
                }
            }

            int apptId = getNewID();
            String apptType = addApptTypeField.getText();
            String apptDescription = addApptDescriptionField.getText();
            String apptLocation = addApptLocationField.getText();
            String apptTitle = addApptTitleField.getText();
            int customerId = addApptCustomerIdChoice.getValue();
            int userId = addApptUserIdChoice.getValue();
            String contact = addApptContactChoice.getValue();

            LocalDateTime currentDateTime = LocalDateTime.now();

            // Format the current date and time as a string
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String createdDateTime = currentDateTime.format(formatter);

            //Get Contact ID from contact name

            //Get UserID

            Appointment appointment = new Appointment(apptId, apptTitle, apptDescription, apptLocation, apptType, utcStartDateTime, utcEndDateTime, createdDateTime, contact, createdDateTime, contact, customerId, userId, int contactId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to handle when cancel button is clicked and return back to the main screen
     * @param actionEvent
     * @throws IOException
     */
    public void cancelButtonClicked(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you would like to cancel adding an Appointment?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if(alert.getResult() == ButtonType.YES) {
            Parent mainScreenWindow = FXMLLoader.load(getClass().getResource("../view/mainScreen.fxml"));
            Scene mainScreenScene = new Scene(mainScreenWindow);
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(mainScreenScene);
            window.show();
        }
    }

    /**
     * Method created to populate the start and end time combo boxes
     */
    private void populateTimeChoices() {
        // Set the initial time to 00:00 (midnight)
        LocalTime startTime = LocalTime.of(0, 0);

        // Set the increment value for the time options (15 minutes)
        int minutesIncrement = 15;

        // Calculate the number of time options for 24 hours (60 minutes / increment)
        int numTimeOptions = 24 * 60 / minutesIncrement;

        // Create a list to store the time options
        ObservableList<LocalTime> timeOptions = FXCollections.observableArrayList();

        // Generate the time options in increments of 15 minutes
        for (int i = 0; i < numTimeOptions; i++) {
            timeOptions.add(startTime);
            startTime = startTime.plusMinutes(minutesIncrement);
        }

        // Set the time options for the ComboBoxes
        addApptStartTimeChoice.setItems(timeOptions);
        addApptEndTimeChoice.setItems(timeOptions);

    }

    public static int getNewID() throws SQLException {
        int apptID = 1;
        for(int i = 0; i < AppointmentDb.getAllAppointments().size(); i++){
            apptID++;
        }
        return apptID;
    }

    /**
     * initialize method to populate the different combo boxes from database table queries
     * @throws SQLException
     */
    @FXML
    public void initialize() throws SQLException {
        ObservableList<Contacts> contactsList = ContactsDb.getAllContacts();
        ObservableList<String> contactNamesList = FXCollections.observableArrayList();
        for(Contacts contact : contactsList){
            String contactName = contact.getContactName();
            contactNamesList.add(contactName);
        }

        ObservableList<Users> userList = UsersDb.getAllUsers();
        ObservableList<Integer> userIDList = FXCollections.observableArrayList();
        for(Users user : userList){
            int userID = user.getUserID();
            userIDList.add(userID);
        }

        ObservableList<Customer> customerList = CustomerDb.getAllCustomers();
        ObservableList<Integer> customerIDList = FXCollections.observableArrayList();
        for(Customer customer : customerList){
            int customerID = customer.getCustId();
            customerIDList.add(customerID);
        }

        addApptContactChoice.setItems(contactNamesList);
        addApptUserIdChoice.setItems(userIDList);
        addApptCustomerIdChoice.setItems(customerIDList);
        populateTimeChoices();

    }


}
