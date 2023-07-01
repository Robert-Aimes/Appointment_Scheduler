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
import sample.DAO.*;
import sample.model.*;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.time.chrono.ChronoZonedDateTime;
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
        try {

            if (!validateAppointmentFields(addApptStartTimeChoice.getValue(), addApptEndTimeChoice.getValue(), addApptStartDatePicker.getValue(), addApptEndDatePicker.getValue(), addApptTypeField.getText(), addApptDescriptionField.getText(), addApptLocationField.getText(), addApptTitleField.getText(), addApptCustomerIdChoice.getValue(), addApptUserIdChoice.getValue(), addApptContactChoice.getValue())) {
                return;
            }
            LocalTime apptStartTime = addApptStartTimeChoice.getValue();
            LocalTime apptEndTime = addApptEndTimeChoice.getValue();
            LocalDate startDate = addApptStartDatePicker.getValue();
            LocalDate endDate = addApptEndDatePicker.getValue();

            if (startDate.isAfter(endDate)) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "The start date cannot be after the end date.", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            if (startDate.isEqual(endDate)) {
                if (apptStartTime.isAfter(apptEndTime)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "The start time cannot be after the end time on the same day.", ButtonType.OK);
                    alert.showAndWait();
                    return;
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Start and end times must be on the same day.", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            LocalDateTime startDateTime = LocalDateTime.of(startDate, apptStartTime);
            LocalDateTime endDateTime = LocalDateTime.of(endDate, apptEndTime);

            // Get the user's time zone
            ZoneId userTimeZone = ZoneId.systemDefault();

            // Convert the user's local time to UTC
            ZonedDateTime userStartDateTime = ZonedDateTime.of(startDateTime, userTimeZone);
            ZonedDateTime userEndDateTime = ZonedDateTime.of(endDateTime, userTimeZone);
            ZonedDateTime utcStartDateTime = userStartDateTime.withZoneSameInstant(ZoneOffset.UTC);
            ZonedDateTime utcEndDateTime = userEndDateTime.withZoneSameInstant(ZoneOffset.UTC);

            String utcStartTime = convertToUTC(startDate + " " + apptStartTime + ":00");
            String utcEndTime = convertToUTC(endDate + " " + apptEndTime + ":00");



            ZoneId estTimeZone = ZoneId.of("America/New_York");
            // Convert appointment start and end times to EST
            ZonedDateTime estStartDateTime = startDateTime.atZone(userTimeZone).withZoneSameInstant(estTimeZone);
            ZonedDateTime estEndDateTime = endDateTime.atZone(userTimeZone).withZoneSameInstant(estTimeZone);

            LocalTime estBusinessHoursStart = LocalTime.of(8, 0);
            LocalTime estBusinessHoursEnd = LocalTime.of(22, 0);
            if (estStartDateTime.toLocalTime().isBefore(estBusinessHoursStart) ||
                    estEndDateTime.toLocalTime().isAfter(estBusinessHoursEnd)) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Appointments must be scheduled between 8:00 a.m. and 10:00 p.m. EST.", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            List<Appointment> existingAppointments = AppointmentDb.getAllAppointments();

            for (Appointment appointment : existingAppointments) {
                LocalDateTime existingStartDateTime = appointment.getApptStartTime();
                LocalDateTime existingEndDateTime = appointment.getApptEndTime();

                // Convert existing appointment times from UTC to user's local time
                ZonedDateTime existingStartZoned = existingStartDateTime.atZone(ZoneOffset.UTC).withZoneSameInstant(userTimeZone);
                LocalDateTime existingStartLocal = existingStartZoned.toLocalDateTime();

                ZonedDateTime existingEndZoned = existingEndDateTime.atZone(ZoneOffset.UTC).withZoneSameInstant(userTimeZone);
                LocalDateTime existingEndLocal = existingEndZoned.toLocalDateTime();

                if (appointment.getCustomerId() == addApptCustomerIdChoice.getValue()) {
                    if ((startDateTime.isAfter(existingStartLocal) && startDateTime.isBefore(existingEndLocal)) ||
                            (endDateTime.isAfter(existingStartLocal) && endDateTime.isBefore(existingEndLocal)) ||
                            (startDateTime.isBefore(existingStartLocal) && endDateTime.isAfter(existingEndLocal))) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Overlapping appointments for a customer is not allowed.", ButtonType.OK);
                        alert.showAndWait();
                        return;
                    }
                }
            }

            int apptId = Integer.parseInt(String.valueOf((int) (Math.random() * 1000)));
            String apptType = addApptTypeField.getText();
            String apptDescription = addApptDescriptionField.getText();
            String apptLocation = addApptLocationField.getText();
            String apptTitle = addApptTitleField.getText();
            int customerId = addApptCustomerIdChoice.getValue();
            int userId = addApptUserIdChoice.getValue();
            String contactName = addApptContactChoice.getValue();
            String createdBy = SharedData.getEnteredUsername();
            String lastUpdatedBy = createdBy;
            int contactId = getContactIdByName(contactName);
            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String createdDateTime = currentDateTime.format(formatter);



            Appointment appointment = new Appointment(apptId, apptTitle, apptDescription, apptLocation, apptType, utcStartDateTime.toLocalDateTime(), utcEndDateTime.toLocalDateTime(), currentDateTime, createdBy, currentDateTime, lastUpdatedBy, customerId, userId, contactId);
            String insertStatement = "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            Connection connection = JDBC.openConnection();
            PreparedStatement ps = connection.prepareStatement(insertStatement);
            ps.setInt(1, apptId);
            ps.setString(2, apptTitle);
            ps.setString(3, apptDescription);
            ps.setString(4, apptLocation);
            ps.setString(5, apptType);
            ps.setTimestamp(6, Timestamp.valueOf(utcStartTime));
            ps.setTimestamp(7, Timestamp.valueOf(utcEndTime));
            ps.setTimestamp(8, Timestamp.valueOf(currentDateTime));
            ps.setString(9, createdBy);
            ps.setTimestamp(10, Timestamp.valueOf(currentDateTime));
            ps.setString(11, lastUpdatedBy);
            ps.setInt(12, customerId);
            ps.setInt(13, userId);
            ps.setInt(14, contactId);
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

    /**
     * Method to create a new appt ID
     * @return
     * @throws SQLException
     */
    public static int getNewID() throws SQLException {
        int newApptID = 0;
        ObservableList<Appointment> apptList = AppointmentDb.getAllAppointments();
        for(Appointment appointment : apptList){
            int apptID = appointment.getApptId();
            if(apptID > newApptID){
                newApptID = apptID + 1;
            }
        }
        return newApptID;
    }

    /**
     * Method to find contactID given the contact name from the combobox
     * @param contactName
     * @return
     * @throws SQLException
     */
    public static int getContactIdByName(String contactName) throws SQLException {

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

    /**
     * Lambda expression to convert a given String to UTC time format
     * @param dateTime
     * @return
     */
    public static String convertToUTC(String dateTime) {
        return Timestamp.valueOf(dateTime)
                .toLocalDateTime()
                .atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneId.of("UTC"))
                .toLocalDateTime()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    /**
     * Method to check if all fxml fields are completed when clicking te save button
     * @param startTime
     * @param endTime
     * @param startDate
     * @param endDate
     * @param type
     * @param description
     * @param location
     * @param title
     * @param customerId
     * @param userId
     * @param contactName
     * @return
     */
    private boolean validateAppointmentFields(LocalTime startTime, LocalTime endTime, LocalDate startDate, LocalDate endDate,
                                              String type, String description, String location, String title, Integer customerId, Integer userId, String contactName) {

        if (startTime == null || endTime == null || startDate == null || endDate == null ||
                type.isEmpty() || description.isEmpty() || location.isEmpty() || title.isEmpty() ||
                contactName == null || customerId == null || userId == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please fill in all the required fields.", ButtonType.OK);
            alert.showAndWait();
            return false; // Return false if any field is blank
        }

        return true; // All fields are valid
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
