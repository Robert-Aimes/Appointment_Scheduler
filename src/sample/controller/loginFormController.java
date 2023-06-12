package sample.controller;

import javafx.fxml.FXML;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class loginFormController {

    @FXML private TextField username;
    @FXML private TextField password;
    @FXML private Button loginButton;


    /*
    This method handles the functionality when the log in button is clicked. Queries the db for correct username and password
     */

    public void loginButtonClicked(ActionEvent event) throws IOException{
        String enteredUsername = username.getText();
        String enteredPassword = password.getText();

    }
}
