package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import sample.DAO.JDBC;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.*;

public class loginFormController {

    @FXML private TextField username;
    @FXML private TextField password;
    @FXML private Button loginButton;


    /**
     * This method handles the functionality when the log in button is clicked. Queries the db for correct username and password
     * @param event
     * @throws IOException
     */
    public void loginButtonClicked(ActionEvent event) throws IOException{
        String enteredUsername = username.getText();
        String enteredPassword = password.getText();

        if(enteredUsername.equals("") || enteredPassword.equals("")){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Username or Password not entered. Please enter both a Username and Password.", ButtonType.OK);
            alert.showAndWait();
        }
        else if(authenticateUser(enteredUsername, enteredPassword)){
            Parent mainScreenWindow = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
            Scene mainScreenScene = new Scene(mainScreenWindow);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(mainScreenScene);
            window.show();

        } else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Username or Password not correct. Please try again.", ButtonType.OK);
        }



    }

    private boolean authenticateUser(String username, String password) {
        try (Connection connection = JDBC.openConnection()) {
            String query = "SELECT password FROM users WHERE username = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String storedPassword = resultSet.getString("password");
                        return storedPassword.equals(password);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
