package Customer.Class;

import Business.SwitchScene;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.io.IOException;


import Customer.SwitchScene;

public class ComingSoonController {

    @FXML
    private Button btn_cart;

    @FXML
    private Button btn_food;

    @FXML
    private Button btn_profile;

    @FXML
    void toCart(ActionEvent event) throws IOException {
        SwitchScene.switchScene(event, "/Customer/FXML/Cart.fxml");

    }

    @FXML
    void toHome(ActionEvent event) throws IOException {
        SwitchScene.switchScene(event, "/Customer/FXML/Home.fxml");

    }

    @FXML
    void toProfile(ActionEvent event) throws IOException {
        SwitchScene.switchScene(event, "/Customer/FXML/Profile.fxml");

    }

}
