import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import java.io.IOException;

import Customer.SwitchScene;

public class LandingPageController {

    @FXML
    private Button btn_ubExpress;

    @FXML
    private Button btn_ubExpressB;

    @FXML
    private void handleCustomerButton(ActionEvent event) throws IOException {
        SwitchScene.switchScene(event, "/Customer/FXML/StartUp.fxml");
    }

    @FXML
    private void handleBusinessButton(ActionEvent event) throws IOException {
        SwitchScene.switchScene(event, "/Business/FXML/BusinessLogin.fxml");
    }
}
