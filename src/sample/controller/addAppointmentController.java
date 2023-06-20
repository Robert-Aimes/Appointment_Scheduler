package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

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
    private ComboBox addApptStartTimeChoice;
    @FXML
    private ComboBox addApptEndTimeChoice;
    @FXML
    private ComboBox addApptCustomerIdChoice;
    @FXML
    private ComboBox addApptUserIdChoice;
    @FXML
    private ComboBox addApptContactChoice;
    @FXML
    private Button addApptSaveButton;
    @FXML
    private Button addApptCancelButton;


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
}
