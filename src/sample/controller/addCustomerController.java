package sample.controller;

import com.mysql.cj.xdevapi.Warning;
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
import sample.DAO.JDBC;
import sample.DAO.firstLevelDivisionsDb;
import sample.model.Contacts;
import sample.model.Countries;
import sample.model.SharedData;
import sample.model.firstLevelDivisions;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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

    /**
     * Method to handle functionality when user presses the save button. Will gather all inputted data and create new customer if no errors are thrown.
     * @param actionEvent
     * @throws IOException
     */
    public void saveCustomerButtonClicked(ActionEvent actionEvent) throws IOException{
        try{

            LocalDateTime currentDateTime = LocalDateTime.now();



            int customerId = Integer.parseInt(String.valueOf((int) (Math.random() * 100)));
            String customerName = addCustNameField.getText();
            String customerPhone = addCustPhoneField.getText();
            String customerAddress = addCustAddressField.getText();
            String customerCountry = (String) addCustCountryField.getValue();
            String customerState = (String) addCustStateField.getValue();
            String customerPostal = addCustPostalField.getText();
            String createdBy = SharedData.getEnteredUsername();
            String lastUpdatedBy = createdBy;
            int divisionId = getDivisionIdByName(customerState);

            checkAddressFormat(customerCountry, customerAddress);

            String insertStatement = "INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES (?,?,?,?,?,?,?,?,?,?)";

            Connection connection = JDBC.openConnection();
            PreparedStatement ps = connection.prepareStatement(insertStatement);
            ps.setInt(1, customerId);
            ps.setString(2, customerName);
            ps.setString(3, customerAddress);
            ps.setString(4, customerPostal);
            ps.setString(5,customerPhone);
            ps.setTimestamp(6, Timestamp.valueOf(currentDateTime));
            ps.setString(7, createdBy);
            ps.setTimestamp(8, Timestamp.valueOf(currentDateTime));
            ps.setString(9, createdBy);
            ps.setInt(10, divisionId);


            //System.out.println("ps " + ps);
            ps.execute();

            Parent mainScreenWindow = FXMLLoader.load(getClass().getResource("../view/mainScreen.fxml"));
            Scene mainScreenScene = new Scene(mainScreenWindow);
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(mainScreenScene);
            window.show();


        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * Method to retrieve Division ID given a division as a parameter
     * @param customerState
     * @return
     * @throws SQLException
     */
    private int getDivisionIdByName(String customerState) throws SQLException{
        int divisionID = -1;
        ObservableList<firstLevelDivisions> divisionsList = firstLevelDivisionsDb.getAllDivisions();
        for(firstLevelDivisions division : divisionsList) {
            if (division.getDivisionName().equals(customerState)) {
                divisionID = division.getDivisionId();
                break;
            }
        }
        return divisionID;

    }

    /**
     * This method populates the State/Province combobox drop down based on User's country selection
     * @param actionEvent
     */
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

    /**
     * Method I created to check the format of the User entered Address text field to see if it is valid for each country.
     * @param customerCountry
     * @param countryAddressField
     */
    public void checkAddressFormat(String customerCountry, String countryAddressField){
        String countryName = (String) addCustCountryField.getValue();
        String customerAddress = addCustAddressField.getText();
        String usCaPattern = "\\d+ [A-Za-z\\s]+, [A-Za-z\\s]+";
        String ukPattern = "\\d+ [A-Za-z\\s]+, [A-Za-z\\s]+, [A-Za-z\\s]+";

        if(countryName.equals("US")){
            if(!customerAddress.matches(usCaPattern)){
                Alert alert = new Alert(Alert.AlertType.WARNING, "Address field must match the format of: 123 ABC Street, White Plains", ButtonType.OK);
                alert.showAndWait();
            }
        } else if(countryName.equals("Canada")){
            if(!customerAddress.matches(usCaPattern)) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Address field must match the format of: 123 ABC Street, Newmarket", ButtonType.OK);
                alert.showAndWait();
            }
        } else if(countryName.equals("UK")){
            if(!customerAddress.matches(ukPattern)) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Address field must match the format of: 123 ABC Street, Greenwich, London", ButtonType.OK);
                alert.showAndWait();
            }
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
