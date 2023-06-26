package sample.controller;

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
import sample.DAO.*;
import sample.model.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class modifyAppointmentController {
    @FXML
    private TextField modifyApptIdField;
    @FXML
    private TextField modifyApptTypeField;
    @FXML
    private TextField modifyApptDescriptionField;
    @FXML
    private TextField modifyApptLocationField;
    @FXML
    private TextField modifyApptTitleField;
    @FXML
    private DatePicker modifyApptStartDatePicker;
    @FXML
    private DatePicker modifyApptEndDatePicker;
    @FXML
    private ComboBox<LocalTime> modifyApptStartTimeChoice;
    @FXML
    private ComboBox<LocalTime> modifyApptEndTimeChoice;
    @FXML
    private ComboBox<Integer> modifyApptCustomerIdChoice;
    @FXML
    private ComboBox<Integer> modifyApptUserIdChoice;
    @FXML
    private ComboBox<String> modifyApptContactChoice;
    @FXML
    private Button modifyApptSaveButton;
    @FXML
    private Button modifyApptCancelButton;
    private Appointment selectedAppointment;

    /**
     * Method to handle functionality when save button is clicked. Populate Appointment database table with all new appt data.
     * @param actionEvent
     * @throws IOException
     */
    public void saveButtonClicked(ActionEvent actionEvent) {
        try{
            LocalTime apptStartTime = modifyApptStartTimeChoice.getValue();
            LocalTime apptEndTime = modifyApptEndTimeChoice.getValue();

            // Get the selected start and end dates from the date pickers
            LocalDate startDate = modifyApptStartDatePicker.getValue();
            LocalDate endDate = modifyApptEndDatePicker.getValue();

            // Check if the start date is after the end date
            if (startDate.isAfter(endDate)) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "The start date cannot be after the end date.", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            // Check if the start date is the same as the end date
            if (startDate.isEqual(endDate)) {
                // Check if the start time is after the end time
                if (apptStartTime.isAfter(apptEndTime)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "The start time cannot be after the end time on the same day.", ButtonType.OK);
                    alert.showAndWait();
                    return;
                }
            }

            // Rest of your code...

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

                // Check if the appointments belong to the same customer
                if (appointment.getCustomerId() == modifyApptCustomerIdChoice.getValue()) {
                    // Check for overlapping appointments
                    if ((utcStartDateTime.isAfter(existingStartDateTime) && utcStartDateTime.isBefore(existingEndDateTime)) ||
                            (utcEndDateTime.isAfter(existingStartDateTime) && utcEndDateTime.isBefore(existingEndDateTime)) ||
                            (utcStartDateTime.isEqual(existingStartDateTime) || utcEndDateTime.isEqual(existingEndDateTime))) {

                        // Display an error message for overlapping appointments
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Overlapping appointments are not allowed.", ButtonType.OK);
                        alert.showAndWait();
                        return;
                    }
                }
            }

            int apptId = Integer.parseInt(modifyApptIdField.getText());
            String apptType = modifyApptTypeField.getText();
            String apptDescription = modifyApptDescriptionField.getText();
            String apptLocation = modifyApptLocationField.getText();
            String apptTitle = modifyApptTitleField.getText();
            int customerId = modifyApptCustomerIdChoice.getValue();
            int userId = modifyApptUserIdChoice.getValue();
            String contactName = modifyApptContactChoice.getValue();
            String createdBy = SharedData.getEnteredUsername();
            String lastUpdatedBy = createdBy;

            int contactId = getContactIdByName(contactName);

            LocalDateTime currentDateTime = LocalDateTime.now();

            // Format the current date and time as a string
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String createdDateTime = currentDateTime.format(formatter);

            //Get UserID need to have query getting the ID from the entered username in logincontroller

            Appointment appointment = new Appointment(apptId, apptTitle, apptDescription, apptLocation, apptType, utcStartDateTime, utcEndDateTime, currentDateTime, createdBy, currentDateTime, lastUpdatedBy, customerId, userId, contactId);

            String updateStatement = "UPDATE appointments SET Appointment_ID = ?, Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Create_Date = ?, Created_By = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";

            Connection connection = JDBC.openConnection();
            PreparedStatement ps = connection.prepareStatement(updateStatement);
            ps.setInt(1, apptId);
            ps.setString(2, apptTitle);
            ps.setString(3, apptDescription);
            ps.setString(4, apptLocation);
            ps.setString(5,apptType);
            //ps.setTimestamp(6, Timestamp.valueOf(startLocalDateTimeToAdd));
            ps.setTimestamp(6, Timestamp.valueOf(utcStartDateTime));
            ps.setTimestamp(7, Timestamp.valueOf(utcEndDateTime));
            //need to verify this is correct
            ps.setTimestamp(8, Timestamp.valueOf(currentDateTime));
            ps.setString(9, createdBy);
            ps.setTimestamp(10, Timestamp.valueOf(currentDateTime));
            ps.setString(11, lastUpdatedBy);
            ps.setInt(12, customerId);
            ps.setInt(13, userId);
            ps.setInt(14, contactId);
            ps.setInt(15, apptId);

            //System.out.println("ps " + ps);
            ps.execute();

            Parent mainScreenWindow = FXMLLoader.load(getClass().getResource("../view/mainScreen.fxml"));
            Scene mainScreenScene = new Scene(mainScreenWindow);
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(mainScreenScene);
            window.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to convert the contact ID from the database table into the contact name to populate the combo box
     * @param contactName
     * @return
     * @throws SQLException
     */
    private int getContactIdByName(String contactName) throws SQLException {
        int contactID = -1;
        ObservableList<Contacts> contactsList = ContactsDb.getAllContacts();
        for(Contacts contact : contactsList) {
            if (contact.getContactName().equals(contactName)) {
                contactID = contact.getContactId();
                break;
            }
        }
        return contactID;
    }

    private String getContactNameByID(int contactID) throws SQLException{
        String contactName = "";
        ObservableList<Contacts> contactsList = ContactsDb.getAllContacts();
        for(Contacts contact : contactsList) {
            if (contact.getContactId() == contactID) {
                contactName = contact.getContactName();
                break;
            }
        }
        return contactName;

    }

    /**
     * Method to return user back to the main screen
     * @param actionEvent
     * @throws IOException
     */
    public void cancelButtonClicked(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you would like to cancel updateing this Appointment?", ButtonType.YES, ButtonType.NO);
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
     * Method to populate all fields with data from selected appointment on the main screen
     * @param selectedAppointment
     * @throws SQLException
     */
    public void setAppointment(Appointment selectedAppointment) throws SQLException {
        this.selectedAppointment = selectedAppointment;
        modifyApptIdField.setText(Integer.toString(selectedAppointment.getApptId()));
        modifyApptTitleField.setText(selectedAppointment.getApptTitle());
        modifyApptDescriptionField.setText(selectedAppointment.getApptDescription());
        modifyApptLocationField.setText(selectedAppointment.getApptLocation());
        modifyApptTypeField.setText(selectedAppointment.getApptType());

        LocalDateTime startDateTime = selectedAppointment.getApptStartTime();

// Extract the date and time components
        LocalDate startDate = startDateTime.toLocalDate();
        LocalTime startTime = startDateTime.toLocalTime();

// Set the date and time values in the fields
        modifyApptStartDatePicker.setValue(startDate);
        modifyApptStartTimeChoice.setValue(startTime);

        LocalDateTime endDateTime = selectedAppointment.getApptEndTime();

// Extract the date and time components
        LocalDate endDate = endDateTime.toLocalDate();
        LocalTime endTime = endDateTime.toLocalTime();

// Set the date and time values in the fields
        modifyApptEndDatePicker.setValue(endDate);
        modifyApptEndTimeChoice.setValue(endTime);

        int contactID = selectedAppointment.getContactId();
        String contactName = getContactNameByID(contactID);
        modifyApptContactChoice.getSelectionModel().select(contactName);
        modifyApptUserIdChoice.setValue(selectedAppointment.getUserId());
        modifyApptCustomerIdChoice.setValue(selectedAppointment.getCustomerId());

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
        modifyApptStartTimeChoice.setItems(timeOptions);
        modifyApptEndTimeChoice.setItems(timeOptions);

    }

    /**
     * Initialize method to populate combo boxes with selections from database tables
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

        modifyApptContactChoice.setItems(contactNamesList);
        modifyApptUserIdChoice.setItems(userIDList);
        modifyApptCustomerIdChoice.setItems(customerIDList);
        populateTimeChoices();

    }
}
