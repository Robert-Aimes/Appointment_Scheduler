package sample.controller;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.DAO.AppointmentDb;
import sample.DAO.ContactsDb;
import sample.DAO.JDBC;
import sample.model.Appointment;
import sample.model.AppointmentSummary;
import sample.model.Contacts;
import sample.model.CustomersByState;

import javax.security.auth.callback.Callback;
import java.io.IOException;
import java.sql.*;

public class reportsController {
    @FXML private TableColumn schedApptIdField;
    @FXML private TableColumn schedTitleField;
    @FXML private TableColumn schedTypeField;
    @FXML private TableColumn schedDescriptionField;
    @FXML private TableColumn schedStartDateField;
    @FXML private TableColumn SchedStartTimeField;
    @FXML private TableColumn schedEndDateField;
    @FXML private TableColumn schedEndTimeField;
    @FXML private TableColumn schedCustIdField;
    @FXML private TableColumn<AppointmentSummary, String> apptMonthField;
    @FXML private TableColumn<AppointmentSummary, String> apptTypeField;
    @FXML private TableColumn<AppointmentSummary, Integer> totalApptField;
    @FXML private TableColumn<CustomersByState, String> custStateField;
    @FXML private TableColumn<CustomersByState, Integer> totalCustField;
    @FXML private ChoiceBox contactChoice;
    @FXML private Button reportsExitButton;
    @FXML private TableView employeeScheduleTable;
    @FXML private TableView summarizedAppointmentTable;
    @FXML private TableView customerByStateTable;

    /**
     * Method that changes filters the employee schedule table based on the contact selected in the combo box
     * @throws SQLException
     */
    public void contactChoice() throws SQLException {
        String chosenContact = (String) contactChoice.getValue();
        int contactID = getContactIdByName(chosenContact);

        ObservableList<Appointment> apptList = AppointmentDb.getAllAppointments();
        ObservableList<Appointment> employeeSchedule = FXCollections.observableArrayList();

        for(Appointment appt : apptList){
            if(appt.getContactId() == contactID){
                employeeSchedule.add(appt);
            }
        }

        employeeScheduleTable.setItems(employeeSchedule);

        schedApptIdField.setCellValueFactory(new PropertyValueFactory<>("apptId"));
        schedTitleField.setCellValueFactory(new PropertyValueFactory<>("apptTitle"));
        schedTypeField.setCellValueFactory(new PropertyValueFactory<>("apptType"));
        schedDescriptionField.setCellValueFactory(new PropertyValueFactory<>("apptDescription"));
        SchedStartTimeField.setCellValueFactory(new PropertyValueFactory<>("apptStartTime"));
        schedEndTimeField.setCellValueFactory(new PropertyValueFactory<>("apptEndTime"));
        schedCustIdField.setCellValueFactory(new PropertyValueFactory<>("customerId"));

    }

    /**
     * This method queries the appointment table to get a summarized view of appointments by month and type and total to populate the report tableview
     * @return
     */
    public ObservableList<AppointmentSummary> getAppointmentSummaries() {
        ObservableList<AppointmentSummary> appointmentSummaries = FXCollections.observableArrayList();

        try (Connection connection = JDBC.openConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT DATE_FORMAT(Start, '%Y-%m') AS Month, Type, COUNT(*) AS TotalAppointments\n" +
                     "FROM Appointments\n" +
                     "WHERE Customer_ID IS NOT NULL\n" +
                     "GROUP BY Month, Type\n" +
                     "ORDER BY Month, Type")) {

            while (resultSet.next()) {
                String month = resultSet.getString("Month");
                String type = resultSet.getString("Type");
                int totalAppointments = resultSet.getInt("TotalAppointments");

                AppointmentSummary summary = new AppointmentSummary(month, type, totalAppointments);
                appointmentSummaries.add(summary);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return appointmentSummaries;
    }

    public ObservableList<CustomersByState> getCustomersByState() {
        ObservableList<CustomersByState> customersByStates = FXCollections.observableArrayList();
        try (Connection connection = JDBC.openConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT d.Division, COUNT(*) AS TotalCustomers " +
                     "FROM Customers c " +
                     "JOIN First_Level_Divisions d ON c.Division_ID = d.Division_ID " +
                     "GROUP BY d.Division")) {

            while (resultSet.next()) {
                String division = resultSet.getString("Division");
                int totalCustomers = resultSet.getInt("TotalCustomers");

                CustomersByState summary = new CustomersByState(division, totalCustomers);
                customersByStates.add(summary);


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customersByStates;
    }

    /**
     * Method to handle the exit button functionality to return back to main screen
     * @param actionEvent
     * @throws IOException
     */
    public void reportsExitButtonClicked(ActionEvent actionEvent) throws IOException {
        Parent mainScreenWindow = FXMLLoader.load(getClass().getResource("../view/mainScreen.fxml"));
        Scene mainScreenScene = new Scene(mainScreenWindow);
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(mainScreenScene);
        window.show();
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
     * Initialize method to populate the table views upon entering the reports view
     * @throws SQLException
     */
    @FXML
    public void initialize() throws SQLException {

        try {
            employeeScheduleTable.setItems(AppointmentDb.getAllAppointments());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        schedApptIdField.setCellValueFactory(new PropertyValueFactory<>("apptId"));
        schedTitleField.setCellValueFactory(new PropertyValueFactory<>("apptTitle"));
        schedTypeField.setCellValueFactory(new PropertyValueFactory<>("apptType"));
        schedDescriptionField.setCellValueFactory(new PropertyValueFactory<>("apptDescription"));
        SchedStartTimeField.setCellValueFactory(new PropertyValueFactory<>("apptStartTime"));
        schedEndTimeField.setCellValueFactory(new PropertyValueFactory<>("apptEndTime"));
        schedCustIdField.setCellValueFactory(new PropertyValueFactory<>("customerId"));

        ObservableList<Contacts> contactsList = ContactsDb.getAllContacts();
        ObservableList<String> contactNamesList = FXCollections.observableArrayList();

        apptMonthField.setCellValueFactory(new PropertyValueFactory<>("month"));
        apptTypeField.setCellValueFactory(new PropertyValueFactory<>("type"));
        totalApptField.setCellValueFactory(new PropertyValueFactory<>("total"));

        custStateField.setCellValueFactory(new PropertyValueFactory<>("state"));
        totalCustField.setCellValueFactory(new PropertyValueFactory<>("totalCustomers"));

        // Retrieve data and populate table
        ObservableList<AppointmentSummary> appointmentSummaries = getAppointmentSummaries();
        summarizedAppointmentTable.setItems(appointmentSummaries);

        ObservableList<CustomersByState> customersByState = getCustomersByState();
        customerByStateTable.setItems(customersByState);


        for(Contacts contact : contactsList){
            String contactName = contact.getContactName();
            contactNamesList.add(contactName);
        }
        contactChoice.setItems(contactNamesList);


    }
}
