package Customer.StartUp;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignUpController {

    private Stage stage;
    private Scene scene; 
    private Parent root;

    @FXML
    private Button btn_close;

    @FXML
    private Button btn_submit;

    @FXML
    private ComboBox<String> cb_partnerSchools;

    @FXML
    private PasswordField pf_password;

    @FXML
    private TextField tf_email;

    @FXML
    private TextField tf_firstName;

    @FXML
    private TextField tf_lastName;

    @FXML
    private TextField tf_phoneNumber;

    @FXML
    public void initialize() {
        cb_partnerSchools.getItems().addAll(
            "National University Manila",
            "Far Easter University",
            "University of Santo Tomas",
            "Centro Escobar University"
        );
}
    @FXML
    void toStartUpPageHandler(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("StartUp.fxml"));

        root = loader.load();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void submitButtonHandler(ActionEvent event) throws IOException{

        String email = tf_email.getText();
        String password = pf_password.getText();
        String firstName = tf_firstName.getText();
        String lastName = tf_lastName.getText();
        String phoneNumber = tf_phoneNumber.getText();
        String selectedSchool = cb_partnerSchools.getValue();

        System.out.println("Email: " + email);
        System.out.println("Password: " + password);
        System.out.println("FName: " + firstName);
        System.out.println("LName: " + lastName);
        System.out.println("Number: " + phoneNumber);
        System.out.println("School: " + selectedSchool);

        // Validate fields
            // Check if email is in email format (gmail, yahoo, email, outllook, edu)
            // Check if email and number is in use
            // Create the account and insert to database
            // Go back to startup

        FXMLLoader loader = new FXMLLoader(getClass().getResource("StartUp.fxml"));

        root = loader.load();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        

    }

    // Submit button
        // Get text from all fields
        // Pass text to validate function
        // Check if email and password is already in use
        // Create the account
        // Go back to startup

}
