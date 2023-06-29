package sample.controller;

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
import javafx.stage.Stage;
import sample.DAO.ContactsDb;
import sample.model.Contacts;

import java.io.IOException;
import java.sql.SQLException;

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

    public void reportsExitButtonClicked(ActionEvent actionEvent) throws IOException {
        Parent mainScreenWindow = FXMLLoader.load(getClass().getResource("../view/mainScreen.fxml"));
        Scene mainScreenScene = new Scene(mainScreenWindow);
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(mainScreenScene);
        window.show();
    }

    @FXML
    public void initialize() throws SQLException {
        ObservableList<Contacts> contactsList = ContactsDb.getAllContacts();
        ObservableList<String> contactNamesList = FXCollections.observableArrayList();
        for(Contacts contact : contactsList){
            String contactName = contact.getContactName();
            contactNamesList.add(contactName);
        }
        contactChoice.setItems(contactNamesList);


    }
}
