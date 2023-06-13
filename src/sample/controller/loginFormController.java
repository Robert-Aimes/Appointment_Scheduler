package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import sample.DAO.JDBC;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

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
        else{

        }



    }
}
