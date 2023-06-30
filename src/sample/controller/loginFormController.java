package sample.controller;

import com.sun.javafx.stage.EmbeddedWindow;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.DAO.JDBC;
import sample.main.Main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.fxml.Initializable;
import sample.model.SharedData;


import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.Locale;
import java.util.TimeZone;




public class loginFormController implements Initializable{

    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private Button loginButton;
    @FXML private Label title;
    @FXML private Label usernameLabel;
    @FXML private Label passwordLabel;
    @FXML private Label zoneID;
    private String alertTitle;
    private String alertHeader;
    private String alertText;

    private static final String LOGIN_ACTIVITY_FILE = "login_activity.txt";


    /**
     * This method handles the functionality when the log in button is clicked. Queries the db for correct username and password
     *
     * @throws IOException
     */
    public void loginButtonClicked(javafx.event.ActionEvent actionEvent) throws IOException, SQLException {
        String enteredUsername = username.getText();
        String enteredPassword = password.getText();

        boolean isAuthenticated = authenticateUser(enteredUsername, enteredPassword);

        //Calls logLoginAttempt method to capture the log in attempt data in the .txt file
        logLoginAttempt(enteredUsername, isAuthenticated);


        if(enteredUsername.equals("") || enteredPassword.equals("")){
            Locale locale = Locale.getDefault();
            if (locale.getLanguage().equals("fr")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("erreur");
                alert.setHeaderText("erreur");
                alert.setContentText("Nom d'utilisateur ou mot de passe non entré. Veuillez saisir à la fois un nom d'utilisateur et un mot de passe.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Username or Password not entered. Please enter both a Username and Password.", ButtonType.OK);
                alert.showAndWait();
            }
        }
        else if(isAuthenticated){
            SharedData.setEnteredUsername(enteredUsername);
            LocalDateTime logInTime = LocalDateTime.now();
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/mainScreen.fxml"));
            Parent scene = loader.load();
            mainScreenController controller = loader.getController();
            controller.checkAppointmentTimes(logInTime);
            stage.setTitle("Main Screen");
            stage.setScene(new Scene(scene));
            stage.show();
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

                    Locale locale = Locale.getDefault();
                    if (locale.getLanguage().equals("fr")) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("erreur");
                        alert.setHeaderText("erreur");
                        alert.setContentText("Nom d'utilisateur ou mot de passe incorrect. Veuillez réessayer.");
                        alert.showAndWait();
                    } else{
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Username or Password not correct. Please try again.", ButtonType.OK);
                        alert.showAndWait();

                    }

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

    /**
     * Method that logs all log in attempts in a login activity .txt file
     * @param username
     * @param isAuthenticated
     */
    private void logLoginAttempt(String username, boolean isAuthenticated) {
        LocalDateTime timestamp = LocalDateTime.now();
        String status = isAuthenticated ? "Success" : "Failure";
        String logEntry = String.format("[%s] %s - %s", timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), username, status);

        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(LOGIN_ACTIVITY_FILE, true)))) {
            writer.println(logEntry);
        } catch (IOException e) {
            System.out.println("Error writing login activity log: " + e.getMessage());
        }
    }


    /**
     * Method that terminates the application
     * @param actionEvent
     */
    public void exitButtonClicked(ActionEvent actionEvent) {
        Platform.exit();
    }

    /**
     * Grabs resource bundle and translates text based on users locale
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

            // Load the appropriate resource bundle based on the user's locale
        Locale locale = Locale.getDefault();
        TimeZone timezone = TimeZone.getTimeZone(String.valueOf(locale));
        String timeZoneDisplayName = ZoneId.systemDefault().getId();
        zoneID.setText(timeZoneDisplayName);

        resourceBundle = ResourceBundle.getBundle("sample.resources.language", locale);

        loginButton.setText(resourceBundle.getString("loginButton"));
        usernameLabel.setText(resourceBundle.getString("usernameLabel"));
        passwordLabel.setText(resourceBundle.getString("passwordLabel"));
        title.setText(resourceBundle.getString("title"));

    }


}
