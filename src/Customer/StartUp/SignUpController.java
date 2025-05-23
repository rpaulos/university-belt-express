package Customer.StartUp;

import Customer.CustomerDatabaseHandler;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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

        String email = tf_email.getText().trim();
        String password = pf_password.getText().trim();
        String firstName = tf_firstName.getText().trim();
        String lastName = tf_lastName.getText().trim();
        String phoneNumber = tf_phoneNumber.getText().trim();
        String selectedSchool = cb_partnerSchools.getValue();

        if (!email.matches("^[\\w.-]+@students\\.(national-u\\.edu\\.ph|ust\\.edu\\.ph|feu\\.edu\\.ph|ceu\\.edu\\.ph)$")) {
            //showAlert(Alert.AlertType.ERROR, "Invalid Email", "Please use your university email.");
            System.out.println("Invalid Email");
            return;
        }

        if (CustomerDatabaseHandler.validateUniqueEmail(email)) {
            //showAlert(Alert.AlertType.ERROR, "Email Exists", "This email is already registered.");
            System.out.println("Email already registered");
            return;
        }

        if (CustomerDatabaseHandler.validateUniquePhoneNumber(phoneNumber)) {
            //showAlert(Alert.AlertType.ERROR, "Phone Number Exists", "This email is already registered.");
            System.out.println("Invalid phone number");
            return;
        } else {
            System.out.println("Valid number");
        }


        // Validate fields
            // Check if email is in email format (paulosr@students.national-u.edu.ph, paulosr@students.ust.edu.ph, paulosr@students.feu.edu.ph, paulosr@students.ceu.edu.ph))
            // Check if email and number is already in use
            // Create the account and insert to database
        
        // Go back to startup
        // FXMLLoader loader = new FXMLLoader(getClass().getResource("StartUp.fxml"));

        // root = loader.load();

        // stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        // scene = new Scene(root);
        // stage.setScene(scene);
        // stage.show();
    }

    // Submit button
        // Get text from all fields
        // Pass text to validate function
        // Check if email and password is already in use
        // Create the account
        // Go back to startup

}
