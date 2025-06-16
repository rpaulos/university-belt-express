package Customer.Class;

import Customer.CustomerDatabaseHandler;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

import Customer.SwitchScene;

public class SignUpController {

    private Stage stage;
    private Scene scene; 
    private Parent root;

    @FXML
    private Button btn_close;

    @FXML
    private Button btn_submit;

    @FXML
    private ComboBox<String> cb_select_city;

    @FXML
    private TextField tf_zip;

    @FXML
    private PasswordField pf_password;

    @FXML
    private TextField tf_address;

    @FXML
    private TextField tf_email;

    @FXML
    private TextField tf_firstName;

    @FXML
    private TextField tf_lastName;

    @FXML
    private TextField tf_phoneNumber;

    @FXML
    private CheckBox cb_terms;

    @FXML
    public void initialize() {
        cb_select_city.getItems().addAll(
            "Manila City"
        );
    }

    @FXML
    void toStartUpPageHandler(ActionEvent event) throws IOException{
        SwitchScene.switchScene(event, "/Customer/FXML/StartUp.fxml");
    }

    @FXML
    void submitButtonHandler(ActionEvent event) throws IOException{

        // Get value from text fields
        String email = tf_email.getText().trim();
        String password = pf_password.getText().trim();
        String firstName = tf_firstName.getText().trim();
        String lastName = tf_lastName.getText().trim();
        String phoneNumber = tf_phoneNumber.getText().trim();
        String selectedCity = cb_select_city.getValue();
        String address = tf_address.getText().trim();
        String zip = tf_zip.getText().trim();

        // Checks if all fields are filled out
        if (email.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || phoneNumber.isEmpty() || selectedCity == null || address.isEmpty() || zip.isEmpty()) {
            // System.out.println("Please fill out all fields");
            showAlert(AlertType.ERROR, "Form Incomplete", "Please fill out all fields.");
            return;
        }

        // Email domain validation
        if (!email.matches("^[\\w.-]+@gmail\\.com")) {
            // System.out.println("Invalid Email");
            showAlert(AlertType.ERROR, "Invalid Email", "Email must be a valid @gmail.com address.");
            return;
        }

        // Checks if email already exists in the database
        if (CustomerDatabaseHandler.emailExists(email)) {
            // System.out.println("Email already registered");
            showAlert(AlertType.ERROR, "Email Exists", "This email is already registered.");
            return;
        }

        // Checks if phone number is the correct length
        if (!phoneNumber.matches("^09\\d{9}$")) {
            // System.out.println("Invalid phone number.");
            showAlert(AlertType.ERROR, "Invalid Phone Number", "Phone number must start with 09 and contain 11 digits.");
            return;
        }

        // Checks if phone number already exists in the database
        if (CustomerDatabaseHandler.phoneNumberExists(phoneNumber)) {
            // System.out.println("Phone number already registered");
            showAlert(AlertType.ERROR, "Phone Number Exists", "This phone number is already registered.");
            return;
        }

        // Checks if password is less than 6 characters
        if (password.length() < 6) {
            // System.out.println("Password must be at least 6 characters long.");
            showAlert(AlertType.ERROR, "Weak Password", "Password must be at least 6 characters long.");
            return;
        }

        // Checks if zip code is 4 characters long
        if (zip.length() != 4) {
            // System.out.println("Invalid Zip");
            showAlert(AlertType.ERROR, "Invalid ZIP Code", "ZIP code must be 4 digits long.");
            return;
        }

        if (!cb_terms.isSelected()) {
            // System.out.println("You must agree to the terms and conditions.");
            showAlert(AlertType.ERROR, "Terms Not Accepted", "You must agree to the terms and conditions.");
            return;
        }

        String customerLocationID = CustomerDatabaseHandler.generateCustomerLocationID(address, selectedCity, zip);

        CustomerDatabaseHandler.insertCustomerLocation(customerLocationID, selectedCity, address, zip);

        CustomerDatabaseHandler.insertCustomer(email, password, firstName, lastName, phoneNumber, customerLocationID);

        // Pop up message for account created
        showAlert(AlertType.INFORMATION, "Success", "Account created successfully!");

        // Go back to startup page
        SwitchScene.switchScene(event, "/Customer/FXML/StartUp.fxml");
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
