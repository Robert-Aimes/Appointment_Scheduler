package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.DAO.ContactsDb;
import sample.DAO.CountriesDb;
import sample.DAO.firstLevelDivisionsDb;
import sample.model.Contacts;
import sample.model.Countries;
import sample.model.firstLevelDivisions;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class addCustomerController {
    @FXML
    private TextField addCustIdField;
    @FXML
    private TextField addCustNameField;
    @FXML
    private TextField addCustPhoneField;
    @FXML
    private TextField addCustAddressField;
    @FXML
    private ChoiceBox addCustStateField;
    @FXML
    private ChoiceBox addCustCountryField;
    @FXML
    private TextField addCustPostalField;
    @FXML
    private Button addCustSaveButton;
    @FXML
    private Button addCustCancelButton;

    /**
     * Method to handle when cancel button is clicked and return back to the main screen
     * @param actionEvent
     * @throws IOException
     */
    public void cancelButtonClicked(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you would like to cancel adding a Customer?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if(alert.getResult() == ButtonType.YES) {
            Parent mainScreenWindow = FXMLLoader.load(getClass().getResource("../view/mainScreen.fxml"));
            Scene mainScreenScene = new Scene(mainScreenWindow);
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(mainScreenScene);
            window.show();
        }
    }

    public void setStateDropDown(ActionEvent actionEvent){
        try{
            String countrySelection = (String) addCustCountryField.getValue();

            ObservableList<firstLevelDivisions> getAllDivisions = firstLevelDivisionsDb.getAllDivisions();

            ObservableList<String> usDivisions = FXCollections.observableArrayList();
            ObservableList<String> ukDivisions = FXCollections.observableArrayList();
            ObservableList<String> caDivisions = FXCollections.observableArrayList();

            getAllDivisions.forEach(firstLevelDivision -> {
                if (firstLevelDivision.getCountryId() == 1) {
                    usDivisions.add(firstLevelDivision.getDivisionName());
                } else if (firstLevelDivision.getCountryId() == 2) {
                    ukDivisions.add(firstLevelDivision.getDivisionName());
                } else if (firstLevelDivision.getCountryId() == 3) {
                    caDivisions.add(firstLevelDivision.getDivisionName());
                }
            });

            //needs a little revision
            if (countrySelection.equals("U.S")) {
                addCustStateField.setItems(usDivisions);
            }
            else if (countrySelection.equals("UK")) {
                addCustStateField.setItems(ukDivisions);
            }
            else if (countrySelection.equals("Canada")) {
                addCustStateField.setItems(caDivisions);
            }

            if(countrySelection.equals("USA")){

            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() throws SQLException{
        ObservableList<Countries> countryList = CountriesDb.getAllCountries();
        ObservableList<String> countries = FXCollections.observableArrayList();
        for(Countries country : countryList){
            String countryName = country.getCountryName();
            countries.add(countryName);
        }

        addCustCountryField.setItems(countries);

    }


}
