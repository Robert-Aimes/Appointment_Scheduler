package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.DAO.CountriesDb;
import sample.DAO.JDBC;
import sample.DAO.firstLevelDivisionsDb;
import sample.model.Countries;
import sample.model.Customer;
import sample.model.SharedData;
import sample.model.firstLevelDivisions;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;

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

    /**
     * Method that takes selected customer from main screen and populates all data in the fxml fields when updating a customer
     * @param selectedCustomer
     * @throws SQLException
     */
    public void setCustomer(Customer selectedCustomer) throws SQLException {
        this.selectedCustomer = selectedCustomer;
        modifyCustIdField.setText(Integer.toString(selectedCustomer.getCustId()));
        modifyCustNameField.setText(selectedCustomer.getCustName());
        modifyCustPhoneField.setText(selectedCustomer.getCustPhone());
        modifyCustAddressField.setText(selectedCustomer.getCustAddress());
        modifyCustStateField.setValue(selectedCustomer.getCustDivisionName());
        modifyCustPostalField.setText(selectedCustomer.getCustPostalCode());
        int custDivisionId = selectedCustomer.getCustDivisionId();
        String custCountryName = getCountryByDivisionId(custDivisionId);
        modifyCustCountryField.setValue(custCountryName);

    }

    /**
     * Method to handle when cancel button is clicked and return back to the main screen
     * @param actionEvent
     * @throws IOException
     */
    public void cancelButtonClicked(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you would like to cancel updating this Customer?", ButtonType.YES, ButtonType.NO);
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
     * Method to handle functionality when user presses the save button. Will gather all inputted data and update the customer if no errors are thrown.
     * @param actionEvent
     * @throws IOException
     */
    public void saveCustomerButtonClicked(ActionEvent actionEvent) throws IOException{
        try{

            LocalDateTime currentDateTime = LocalDateTime.now();



            int customerId = selectedCustomer.getCustId();
            String customerName = modifyCustNameField.getText();
            String customerPhone = modifyCustPhoneField.getText();
            String customerAddress = modifyCustAddressField.getText();
            String customerCountry = (String) modifyCustCountryField.getValue();
            String customerState = (String) modifyCustStateField.getValue();
            String customerPostal = modifyCustPostalField.getText();
            String createdBy = selectedCustomer.getCreatedBy();
            String lastUpdatedBy = SharedData.getEnteredUsername();
            int divisionId = getDivisionIdByName(customerState);
            LocalDateTime createdDate = selectedCustomer.getCustCreateDate();

            // Validate address format
            boolean isAddressFormatValid = checkAddressFormat(customerCountry, customerAddress);
            if (!isAddressFormatValid) {
                Alert alert = new Alert(Alert.AlertType.WARNING, getInvalidAddressFormatMessage(customerCountry), ButtonType.OK);
                alert.showAndWait();
                return; // Exit the method if address format is invalid
            }

            // Validate input fields
            if (!validateInputFields(customerName, customerPhone, customerAddress, customerCountry, customerState, customerPostal)) {
                return; // Exit the method if any field is invalid
            }


            String updateStatement = "UPDATE customers SET Customer_ID = ?, Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Create_Date = ?, Created_By = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";

            Connection connection = JDBC.openConnection();
            PreparedStatement ps = connection.prepareStatement(updateStatement);
            ps.setInt(1, customerId);
            ps.setString(2, customerName);
            ps.setString(3, customerAddress);
            ps.setString(4, customerPostal);
            ps.setString(5,customerPhone);
            ps.setTimestamp(6, Timestamp.valueOf(createdDate));
            ps.setString(7, createdBy);
            ps.setTimestamp(8, Timestamp.valueOf(currentDateTime));
            ps.setString(9, lastUpdatedBy);
            ps.setInt(10, divisionId);
            ps.setInt(11, customerId);


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
     * LAMBDA EXPRESSION to retrieve Division ID given a division as a parameter
     * @param customerState
     * @return
     * @throws SQLException
     */
    private int getDivisionIdByName(String customerState) throws SQLException {
        ObservableList<firstLevelDivisions> divisionsList = firstLevelDivisionsDb.getAllDivisions();
        return divisionsList.stream()
                .filter(division -> division.getDivisionName().equals(customerState))
                .findFirst()
                .map(firstLevelDivisions::getDivisionId)
                .orElse(-1);
    }

    /**
     * Method I created to check the format of the User entered Address text field to see if it is valid for each country.
     * @param customerCountry
     * @param countryAddressField
     */
    /**
     * Method I created to check the format of the User entered Address text field to see if it is valid for each country.
     * @param customerCountry
     * @param customerAddress
     */
    public boolean checkAddressFormat(String customerCountry, String customerAddress) {
        String usPattern = "\\d+ [A-Za-z\\s]+, [A-Za-z\\s]+";
        String canadaPattern = "\\d+ [A-Za-z\\s]+, [A-Za-z\\s]+";
        String ukPattern = "\\d+ [A-Za-z\\s]+, [A-Za-z\\s]+, [A-Za-z\\s]+";

        if (customerCountry.equals("U.S")) {
            return customerAddress.matches(usPattern);
        } else if (customerCountry.equals("Canada")) {
            return customerAddress.matches(canadaPattern);
        } else if (customerCountry.equals("UK")) {
            return customerAddress.matches(ukPattern);
        }

        return false;
    }

    /**
     * Field to generate custom error message if user entered address does not match the required format per selected Country
     * @param customerCountry
     * @return
     */
    private String getInvalidAddressFormatMessage(String customerCountry) {
        if (customerCountry.equals("U.S")) {
            return "Address field must match the format of: 123 ABC Street, White Plains";
        } else if (customerCountry.equals("Canada")) {
            return "Address field must match the format of: 123 ABC Street, Newmarket";
        } else if (customerCountry.equals("UK")) {
            return "Address field must match the format of: 123 ABC Street, Greenwich, London";
        }return "Address does not match required format for Country.";
    }

    /**
     * Method to check if all fields have entered data from user
     * @param customerName
     * @param customerPhone
     * @param customerAddress
     * @param customerCountry
     * @param customerState
     * @param customerPostal
     * @return
     */
    private boolean validateInputFields(String customerName, String customerPhone, String customerAddress, String customerCountry, String customerState, String customerPostal) {
        if (customerName.isEmpty() || customerPhone.isEmpty() || customerAddress.isEmpty() ||
                customerCountry == null || customerState == null || customerPostal.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please fill in all the required fields.", ButtonType.OK);
            alert.showAndWait();
            return false; // Return false if any field is blank
        }
        return true; // All fields are valid
    }

    /**
     * This method queries the mySQL tables to find the customer's country name given the divisionID.
     * @param divisionID
     * @return
     * @throws SQLException
     */
    private String getCountryByDivisionId(int divisionID) throws SQLException {
        String country = "";

        // Query the database to retrieve the country based on the division ID
        String query = "SELECT C.Country FROM COUNTRIES C " +
                "JOIN FIRST_LEVEL_DIVISIONS D ON C.Country_ID = D.Country_ID " +
                "WHERE D.Division_ID = ?";

        try (Connection connection = JDBC.openConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, divisionID);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    country = resultSet.getString("Country");
                }
            }
        }

        return country;
    }

    /**
     * This method populates the State/Province combobox drop down based on User's country selection
     * @param actionEvent
     */
    private boolean countrySelectionChanged = false;

    public void setStateDropDown(ActionEvent actionEvent) {
        try {
            String countrySelection = (String) modifyCustCountryField.getValue();
            String originalCountrySelection = getCountryByDivisionId(selectedCustomer.getCustDivisionId());

            // Check if the country selection has changed
            if (!countrySelection.equals(originalCountrySelection)) {
                countrySelectionChanged = true;
            }

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
                modifyCustStateField.setItems(usDivisions);
            } else if (countrySelection.equals("UK")) {
                modifyCustStateField.setItems(ukDivisions);
            } else if (countrySelection.equals("Canada")) {
                modifyCustStateField.setItems(caDivisions);
            }

            // Clear the state field value if the country selection has changed
            if (countrySelectionChanged) {
                modifyCustStateField.setValue(null);
                countrySelectionChanged = false; // Reset the flag
            } else {
                // Set the value of the state combo box to the original value
                modifyCustStateField.setValue(selectedCustomer.getCustDivisionName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize method to populate the country options in the combobox drop down
     * @throws SQLException
     */
    @FXML
    public void initialize() throws SQLException{
        ObservableList<Countries> countryList = CountriesDb.getAllCountries();
        ObservableList<String> countries = FXCollections.observableArrayList();
        for(Countries country : countryList){
            String countryName = country.getCountryName();
            countries.add(countryName);
        }

        modifyCustCountryField.setItems(countries);



    }


}
