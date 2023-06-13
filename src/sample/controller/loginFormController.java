package sample.controller;

import com.sun.javafx.stage.EmbeddedWindow;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import sample.DAO.JDBC;
import sample.main.Main;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class loginFormController {

    @FXML private TextField username;
    @FXML private TextField password;
    @FXML private Button loginButton;


    /**
     * This method handles the functionality when the log in button is clicked. Queries the db for correct username and password
     *
     * @throws IOException
     */
    public void loginButtonClicked(javafx.event.ActionEvent actionEvent) throws IOException {
        String enteredUsername = username.getText();
        String enteredPassword = password.getText();

        boolean isAuthenticated = authenticateUser(enteredUsername, enteredPassword);

        if(enteredUsername.equals("") || enteredPassword.equals("")){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Username or Password not entered. Please enter both a Username and Password.", ButtonType.OK);
            alert.showAndWait();
        }
        else if(isAuthenticated){
            openMainScreen();


        } else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Username or Password not correct. Please try again.", ButtonType.OK);
        }
    }

    /**
     * Method to authenticate a user
     * @param username
     * @param password
     * @return
     */
    private boolean authenticateUser(String username, String password) {
        Connection connection = JDBC.openConnection();
        if (connection != null) {
            try {
                String query = "SELECT * FROM users WHERE User_Name = ? AND Password = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, username);
                statement.setString(2, password);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    resultSet.close();
                    statement.close();
                    connection.close();
                    return true;
                }

                resultSet.close();
                statement.close();
            } catch (SQLException e) {
                System.out.println("Error executing query: " + e.getMessage());
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println("Error closing connection: " + e.getMessage());
                }
            }
        } else {
            System.out.println("Error connecting to the database.");
        }

        return false;
    }



    private <MainScreenController> void openMainScreen() {
        try {
            // Load the FXML file for the main screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainScreen.fxml"));
            Parent root = loader.load();

            // Get the controller for the main screen
            MainScreenController mainScreenController = loader.getController();

            // Pass any necessary data to the main screen controller if needed
            // mainScreenController.setData(...);

            // Create a new scene with the loaded FXML file
            Scene mainScene = new Scene(root);

            // Set the scene on the primary stage
            EmbeddedWindow primaryStage = null;
            primaryStage.setScene(mainScene);

            // Show the main screen
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
