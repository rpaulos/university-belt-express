package Customer.Home;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ProfileController {

    @FXML
    private Button btn_addresses;

    @FXML
    private Button btn_favourites;

    @FXML
    private Button btn_fpbusiness;

    @FXML
    private Button btn_helpcenter;

    @FXML
    private Button btn_orders;

    @FXML
    private Button btn_pandapay;

    @FXML
    private Button btn_grocery;

    @FXML
    private Button btn_food;

    @FXML
    private Button btn_search;

    @FXML
    private Button btn_termspolicies;

    private Stage stage;
    private Scene scene; 
    private Parent root;

    @FXML
    public void toHomeHandler(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));

        root = loader.load();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

}
