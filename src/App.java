import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Optional: GUI code goes here
        System.out.println("JavaFX App Started");
        getName();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void getName() {
        String myNumber = "093234566741";
        String myLastName = DatabaseHandler.getLastName(myNumber);

        System.out.println(myLastName);
    }
}
