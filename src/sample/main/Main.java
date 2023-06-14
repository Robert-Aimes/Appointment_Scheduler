package sample.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.DAO.JDBC;

import java.io.IOException;

public class Main extends Application {




    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("../view/loginForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        stage.setTitle("Login Screen");
        stage.setScene(scene);
        stage.show();
    }






    public static void main(String[] args) {

        launch(args);




    }
}
