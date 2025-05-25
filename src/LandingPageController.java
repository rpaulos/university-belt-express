import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import java.io.IOException;

public class LandingPageController {

    @FXML
    private Button btn_ubExpress;

    @FXML
    private Button btn_ubExpressB;

    private void switchScene(ActionEvent event, String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleCustomerButton(ActionEvent event) throws IOException {
        switchScene(event, "/Customer/StartUp/StartUp.fxml");
    }

    @FXML
    private void handleBusinessButton(ActionEvent event) throws IOException {
        switchScene(event, "/Business/BusinessStartUp/BusinessLogin.fxml");
    }
}
