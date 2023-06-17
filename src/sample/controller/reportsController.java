package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;

import java.io.IOException;

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
}
