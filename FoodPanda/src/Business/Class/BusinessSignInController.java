package Business.Class;

import java.io.IOException;

import Business.BusinessDatabaseHandler;
import Business.BusinessSession;

import Business.SwitchScene;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class BusinessSignInController {

    @FXML
    private Button btn_return;

    @FXML
    private Button btn_signIn;

    @FXML
    private PasswordField pf_password;

    @FXML
    private TextField tf_email;

    private Stage stage;
    private Scene scene; 
    private Parent root;

    @FXML
    public void toReturnToSignIntHandler(ActionEvent event) throws IOException{
        SwitchScene.switchScene(event, "/Business/FXML/BusinessLogin.fxml");
    }

    @FXML
    public void toValidateOwnerLogin(ActionEvent event) throws IOException {
        String email = tf_email.getText().trim();
        String password = pf_password.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            System.out.println("Email and password cannot be empty.");
            return;
        }

        if (email.equals("tristan@admin.com")) {
            adminSignIn(event);
        }

        if (BusinessDatabaseHandler.validateBusinessOwnerLogin(email, password)) {
            System.out.println("Login successful!");

            BusinessSession.setEmail(email);
            BusinessSession.setRestaurantID(BusinessDatabaseHandler.getRestaurantID(email));

            SwitchScene.switchScene(event, "/Business/FXML/BusinessHomePage.fxml");
        } else {
            System.out.println("Login failed! Invalid email or password.");
        }
    }

    void adminSignIn(ActionEvent event) throws IOException {
        SwitchScene.switchScene(event, "/Admin/FXML/Admin.fxml");
    }
}
