package Customer.Home;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;

public class HomeController {

    @FXML
    private Button btn_Search;

    @FXML
    private Button btn_account;

    @FXML
    private Button btn_grocery;

    private Stage stage;
    private Scene scene; 
    private Parent root;

    @FXML
    public void toProfilePageHandler(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Profile.fxml"));

        root = loader.load();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

}

