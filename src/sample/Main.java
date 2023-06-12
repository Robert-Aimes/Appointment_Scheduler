package sample;

import sample.DAO.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("LoginForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 600);
        stage.setTitle("Login Screen");
        stage.setScene(scene);
        stage.show();
    }




    public static void main(String[] args) {

        launch(args);


    }
}
