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
    private ComboBox modifyApptStartTimeChoice;
    @FXML
    private ComboBox modifyApptEndTimeChoice;
    @FXML
    private ComboBox modifyApptCustomerIdChoice;
    @FXML
    private ComboBox modifyApptUserIdChoice;
    @FXML
    private ComboBox modifyApptContactChoice;
    @FXML
    private Button modifyApptSaveButton;
    @FXML
    private Button modifyApptCancelButton;


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
}
