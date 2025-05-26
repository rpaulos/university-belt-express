package Business.BusinessStartUp;

import java.io.IOException;

import Business.BusinessDatabaseHandler;
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
    private Button signinbtn;

    @FXML
    private TextField signinemailadd;

    @FXML
    private PasswordField signinpassword;

    @FXML
    private Button signreturnbtn;

    private Stage stage;
    private Scene scene; 
    private Parent root;

    @FXML
    public void toReturnToSignIntHandler(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("BusinessLogin.fxml"));

        root = loader.load();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    public void toValidateOwnerLogin(ActionEvent event) throws IOException {
        String oemail = signinemailadd.getText();
        String opassword = signinpassword.getText();

        if (BusinessDatabaseHandler.validateBusinessOwnerLogin(oemail, opassword)) {
            System.out.println("Login successful!");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Business/BusinessStartUp/BusinessSignUp.fxml")); // update path if needed

            root = loader.load();

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root, 340, 740);
            stage.setScene(scene);
            stage.show();
        } else {
            System.out.println("Login failed! Invalid email or password.");
        }
    }
}
