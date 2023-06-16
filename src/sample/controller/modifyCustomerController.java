package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class modifyCustomerController {
    @FXML
    private TextField modifyCustIdField;
    @FXML
    private TextField modifyCustNameField;
    @FXML
    private TextField modifyCustPhoneField;
    @FXML
    private TextField modifyCustAddressField;
    @FXML
    private ChoiceBox modifyCustStateField;
    @FXML
    private ChoiceBox modifyCustCountryField;
    @FXML
    private TextField modifyCustPostalField;
    @FXML
    private Button modifyCustSaveButton;
    @FXML
    private Button modifyCustCancelButton;
}
