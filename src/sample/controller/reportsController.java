package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import sample.model.Appointment;
import sample.model.Contacts;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class reportsController {
    public TableColumn schedApptIdField;
    public TableColumn schedTitleField;
    public TableColumn schedTypeField;
    public TableColumn schedDescriptionField;
    public TableColumn schedStartDateField;
    public TableColumn SchedStartTimeField;
    public TableColumn schedEndDateField;
    public TableColumn schedEndTimeField;
    public TableColumn schedCustIdField;
    public TableColumn apptMonthField;
    public TableColumn apptTypeField;
    public TableColumn totalApptField;
    public TableColumn custStateField;
    public TableColumn totalCustField;
    public ChoiceBox contactChoice;
    public Button reportsExitButton;
    public TableView employeeScheduleTable;
    public TableView summarizedAppointmentTable;
    public TableView customerByStateTable;

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

    public void populateSummarizedTable(ActionEvent event){

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

        for(Contacts contact : contactsList){
            String contactName = contact.getContactName();
            contactNamesList.add(contactName);
        }
        contactChoice.setItems(contactNamesList);


    }
}
