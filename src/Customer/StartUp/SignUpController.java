package Customer.StartUp;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class SignUpController {

    private Stage stage;
    private Scene scene; 
    private Parent root;

    @FXML
    private Button btn_close;

    @FXML
    void toStartUpPageHandler(ActionEvent event) throws IOException{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StartUp.fxml"));

        root = loader.load();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

}
