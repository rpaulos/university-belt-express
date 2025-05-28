package Business.BusinessStartUp;

import java.io.IOException;

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("BusinessSignIn.fxml"));

        root = loader.load();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    public void toSignUpnWithEmailHandler(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("BusinessSignUp.fxml"));

        root = loader.load();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
    
     @FXML
    public void toReturnOnLandingPage(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/LandingPage.fxml"));

        root = loader.load();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
}

