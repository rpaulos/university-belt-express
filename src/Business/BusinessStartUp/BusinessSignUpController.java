package Business.BusinessStartUp;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class BusinessSignUpController {

    @FXML
    private TextField addresstextf;

    @FXML
    private TextField citytextf;

    @FXML
    private TextField companynametextf;

    @FXML
    private TextField postalcodetextf;

    @FXML
    private TextField signupemailtextf;

    @FXML
    private TextField signupfnametextf;

    @FXML
    private TextField signuplnametextf;

    @FXML
    private PasswordField signuppasswordf;

    @FXML
    private CheckBox termscb;

    private Stage stage;
    private Scene scene; 
    private Parent root;

    @FXML
    public void toReturnToLogintHandler(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("BusinessLogin.fxml"));

        root = loader.load();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

}