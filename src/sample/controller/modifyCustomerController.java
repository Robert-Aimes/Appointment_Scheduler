package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import sample.model.Customer;

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
    private Customer selectedCustomer;

    public void setCustomer(Customer selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
        modifyCustIdField.setText(Integer.toString(selectedCustomer.getCustId()));
        modifyCustNameField.setText(selectedCustomer.getCustName());
        modifyCustPhoneField.setText(selectedCustomer.getCustPhone());
        modifyCustAddressField.setText(selectedCustomer.getCustAddress());
        modifyCustStateField.setValue(selectedCustomer.getCustDivisionName());
        modifyCustPostalField.setText(selectedCustomer.getCustPostalCode());
        //modifyCustCountryField.setValue(selectedCustomer.get)

    }
}
