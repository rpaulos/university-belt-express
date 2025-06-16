package Business.Class;

import Business.BusinessDatabaseHandler;
import java.io.IOException;

import Business.SwitchScene;
import java.awt.Desktop;
import java.nio.Buffer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javafx.scene.control.Alert.AlertType;

public class BusinessSignUpController {

    @FXML
    private Button btn_signUp;

    @FXML
    private CheckBox cb_terms;

    @FXML
    private PasswordField pf_password;

    @FXML
    private TextField tf_address;

    @FXML
    private ComboBox<String> cb_select_city;

    @FXML
    private TextField tf_companyName;

    @FXML
    private TextField tf_emailAddress;

    @FXML
    private TextField tf_firstName;

    @FXML
    private TextField tf_lastName;

    @FXML
    private TextField tf_postalCode;

    private Stage stage;
    private Scene scene; 
    private Parent root;

    @FXML
    public void initialize() {
        cb_select_city.getItems().addAll(
            "Manila City"
        );
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void toReturnToLogintHandler(ActionEvent event) throws IOException{
        SwitchScene.switchScene(event, "/Business/FXML/BusinessLogin.fxml");
    }

    @FXML
    void submitButtonHandler(ActionEvent event) throws IOException {

        // Get value from text fields
        String firstName = tf_firstName.getText().trim();
        String lastName = tf_lastName.getText().trim();
        String email = tf_emailAddress.getText().trim();
        String companyName = tf_companyName.getText().trim();
        String address = tf_address.getText().trim();
        String selectedCity = cb_select_city.getValue();
        String zip = tf_postalCode.getText().trim();
        String password = pf_password.getText().trim();
        boolean termsAccepted = cb_terms.isSelected();

        // Validate input
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || companyName.isEmpty() ||
            address.isEmpty() || selectedCity.isEmpty() || zip.isEmpty() || password.isEmpty()) {
            // Show error message
            return;
        }

        // Email domain validation
        if (!email.matches("^[\\w.-]+@gmail\\.com")) {
            System.out.println("Invalid Email");
            return;
        }

        // Checks if email already exists in the database
        if (BusinessDatabaseHandler.emailExists(email)) {
            System.out.println("Email already exists");
            return;
        }

        // Checks if companyName already exists in the database
        if (BusinessDatabaseHandler.companyNameExists(companyName)) {
            System.out.println("Company name already exists");
            return;
        }

        // Checks if zip code is 4 characters long
        if (zip.length() != 4) {
            System.out.println("Zip code must be 4 characters long");
            return;
        }

        //Checks if password is at least 8 characters long
        if (password.length() < 8) {
            System.out.println("Password must be at least 8 characters long");
            return;
        }

        if (!cb_terms.isSelected()) {
            System.out.println("You must agree to the terms and conditions.");
            return;
        }

        if (!termsAccepted) {
            System.out.println("You must agree to the terms and conditions.");
            return;
        }

        String restaurantLocationID = BusinessDatabaseHandler.generateRestaurantLocationID(address, selectedCity, zip);

        BusinessDatabaseHandler.insertRestaurantLocation(restaurantLocationID, selectedCity, address, zip);

        String restaurantID = BusinessDatabaseHandler.generateRestaurantID(restaurantLocationID);

        String headerPath = "C:\\Users\\Rae\\Desktop\\FoodPanda\\FoodPanda\\src\\User Interface\\Restaurant Header\\" + companyName + ".png";
        BusinessDatabaseHandler.insertRestaurant(restaurantID, restaurantLocationID, companyName, headerPath);

        String businessOwnerID = BusinessDatabaseHandler.generateBusinessOwnerID(restaurantID);

        BusinessDatabaseHandler.insertBusinessOwner(businessOwnerID, restaurantID, firstName, lastName, email, password);

        //System.out.println("Sign up successful!");
        showAlert(Alert.AlertType.INFORMATION, "Success", "Account created!");
        

        SwitchScene.switchScene(event, "/Business/FXML/BusinessLogin.fxml");
        
    }

}