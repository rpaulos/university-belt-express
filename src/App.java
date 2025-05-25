import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import Database.DatabaseHandler;
import Customer;
import Business;

import java.io.IOException;

import Admin;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Customer/StartUp/StartUp.fxml"));

        primaryStage.setTitle("U-Belt Express");
        primaryStage.setScene(new Scene(root, 340, 740));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
