package Customer.Class;

import Customer.CustomerDatabaseHandler;
import Customer.CustomerSession;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import Customer.SwitchScene;

public class LoginController {

    @FXML
    private Button btn_create;

    @FXML
    private Button btn_submit;

    @FXML
    private PasswordField pf_password;

    @FXML
    private TextField tf_email;

    @FXML
    private Button btn_close;

    private Stage stage;
    private Scene scene; 
    private Parent root;

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void toSignUpPageHandler(ActionEvent event) throws IOException{
        SwitchScene.switchScene(event, "/Customer/FXML/SignUp.fxml");

    }

    @FXML
    void toStartUpPageHandler(ActionEvent event) throws IOException{
        SwitchScene.switchScene(event, "/Customer/FXML/StartUp.fxml");

    }

    @FXML
    void toValidateLogin(ActionEvent event) throws IOException{

        String email = tf_email.getText();
        String password = pf_password.getText();

        if (!CustomerDatabaseHandler.emailExists(email)) {
            showAlert(Alert.AlertType.WARNING, "Login Unsuccessful", "Account does not exist. Please sign up.");
            return;
        } 


        if (CustomerDatabaseHandler.validateLoginCredentials(email, password)) {
            // Pop up message for successful login
            System.out.println("Succesful");
            showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Start adding to cart!");

            // Store email in CustomerSession
            CustomerSession.setEmail(email);
            CustomerSession.setCustomerID(CustomerDatabaseHandler.getCustomerID(email));

            SwitchScene.switchScene(event, "/Customer/FXML/Home.fxml");
        } else {
            System.out.println("Unsuccesful");
            showAlert(Alert.AlertType.WARNING, "Login Unsuccessful", "Incorrect Email or password.");
        }
    }
}
