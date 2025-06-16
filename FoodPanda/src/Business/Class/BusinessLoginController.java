package Business.Class;

import java.io.IOException;

import Business.SwitchScene;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class BusinessLoginController {

    @FXML
    private Button logsigninbtn;

    @FXML
    private Button logsignupbtn;

    @FXML
    private Button logreturnbtn;

    private Stage stage;
    private Scene scene; 
    private Parent root;

   @FXML
    public void toSignInWithEmailHandler(ActionEvent event) throws IOException{
        SwitchScene.switchScene(event, "/Business/FXML/BusinessSignIn.fxml");
    }

    @FXML
    public void toSignUpnWithEmailHandler(ActionEvent event) throws IOException{
        SwitchScene.switchScene(event, "/Business/FXML/BusinessSignUp.fxml");
    }

    
    @FXML
    void toLandingPage(ActionEvent event) throws IOException {
        SwitchScene.switchScene(event, "/LandingPage.fxml");
    }
}

