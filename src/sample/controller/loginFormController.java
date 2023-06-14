package sample.controller;

import com.sun.javafx.stage.EmbeddedWindow;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import sample.DAO.JDBC;
import sample.main.Main;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.fxml.Initializable;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.Locale;


public class loginFormController implements Initializable{

    @FXML private TextField username;
    @FXML private TextField password;
    @FXML private Button loginButton;
    @FXML private Label title;
    @FXML private Label usernameLabel;
    @FXML private Label passwordLabel;
    @FXML private Label zoneID;


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
            Alert alert = new Alert(Alert.AlertType.ERROR, "Username or Password not entered. Please enter both a Username and Password.", ButtonType.OK);
            alert.showAndWait();
        }
        else if(isAuthenticated){
            Parent mainScreenWindow = FXMLLoader.load(getClass().getResource("../view/mainScreen.fxml"));
            Scene mainScreenScene = new Scene(mainScreenWindow);
            Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            window.setScene(mainScreenScene);
            window.show();


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
                } else{
                    resultSet.close();
                    statement.close();
                    connection.close();
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Username or Password not correct. Please try again.", ButtonType.OK);
                    alert.showAndWait();
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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

            // Load the appropriate resource bundle based on the user's locale
        Locale locale = Locale.getDefault();
        resourceBundle = ResourceBundle.getBundle("resources/language", locale);


        usernameLabel.setText(resourceBundle.getString("usernameLabel"));
        passwordLabel.setText(resourceBundle.getString("passwordLabel"));
        loginButton.setText(resourceBundle.getString("loginButton"));
        title.setText(resourceBundle.getString("title"));
            // ...



    }
}
