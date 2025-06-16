package Admin.Class;

import Customer.CustomerDatabaseHandler;
import Customer.CustomerSession;
import Customer.Class.ProductCardController;
import Customer.Class.ProductItem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.List;

import Customer.CustomerDatabaseHandler;

import java.io.File;
import java.io.IOException;
import Customer.CustomerSession;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import java.util.Collections;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

import Customer.SwitchScene;
import java.awt.event.ActionEvent;
import java.awt.image.ImageFilter;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
public class AdminController {

    @FXML
    private Button btn_logout;

    @FXML
    private Button btnCD_search;

    @FXML
    private Button btnCD_update;

    @FXML
    private Button btnOD_delete;

    @FXML
    private Button btnOD_search;

    @FXML
    private Button btnPD_delete;

    @FXML
    private Button bttOD_search;

    @FXML
    private TableColumn<OrderRow, Double> colCD_amount;

    @FXML
    private TableColumn<OrderRow, String> colCD_orderID;

    @FXML
    private TableColumn<OrderRow, String> colCD_restaurantID;

    @FXML
    private TableColumn<OrderRow, Double> colPD_amount;

    @FXML
    private TableColumn<OrderRow, String> colPD_customerID;

    @FXML
    private TableColumn<OrderRow, String> colPD_orderID;

    @FXML
    private TableView<OrderRow> tableCD;

    @FXML
    private TableView<OrderRow> tablePD;

    @FXML
    private AnchorPane gridView;

    @FXML
    private Label lbl_1;

    @FXML
    private Label lbl_2;

    @FXML
    private Label lbl_3;

    @FXML
    private Label lbl_4;

        @FXML
    private Label lbl_5;

    @FXML
    private Label lbl_6;

    @FXML
    private Label lbl_7;

    @FXML
    private Label lbl_8;

    @FXML
    private Label lblCD_customerID;

    @FXML
    private Label lblCD_email;

    @FXML
    private Label lblCD_name;

    @FXML
    private Label lblCD_phoneNumber;

    @FXML
    private Label lblCD_totalSpent;

    @FXML
    private Label lblOD_businessName;

    @FXML
    private Label lblOD_businessOwnerEmail;

    @FXML
    private Label lblOD_businessOwnerID;

    @FXML
    private Label lblOD_restaurantID;

    @FXML
    private Label lblOD_businessOwnerName;

    @FXML
    private Label lblOD_totalEarned;

    @FXML
    private TextField tfCD_customerID;

    @FXML
    private TextField tfCD_firstName;

    @FXML
    private TextField tfCD_lastName;

    @FXML
    private TextField tfCD_phoneNumber;

    @FXML
    private TextField tfOD_businessOwnerID;

    @FXML
    private TextField tfOD_firstName;

    @FXML
    private TextField tfOD_lastName;

    @FXML
    private TextField tfOD_restaurantID;

    @FXML
    private TextField tfPD_productID;

    @FXML
    private TextField tfPD_restaurantID;

    @FXML
    private GridPane cardGrid;

    private Stage stage;
    private Scene scene; 
    private Parent root;

    private static String customerID;
    private static String restaurantID;



    public void initialize() {
        colCD_orderID.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        colCD_restaurantID.setCellValueFactory(new PropertyValueFactory<>("restaurantID"));
        colCD_amount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        colPD_orderID.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        colPD_customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        colPD_amount.setCellValueFactory(new PropertyValueFactory<>("amount"));    
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/LandingPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


// --------------------------------------------------------------------------------------------------- //
    
    private void setCustomerLabelsVisible(boolean visible) {
        lblCD_name.setVisible(visible);
        lblCD_customerID.setVisible(visible);
        lblCD_email.setVisible(visible);
        lblCD_phoneNumber.setVisible(visible);
        lblCD_totalSpent.setVisible(visible);

        lblCD_name.setManaged(visible);
        lblCD_customerID.setManaged(visible);
        lblCD_email.setManaged(visible);
        lblCD_phoneNumber.setManaged(visible);
        lblCD_totalSpent.setManaged(visible);

        lbl_1.setVisible(visible);
        lbl_2.setVisible(visible);
        lbl_3.setVisible(visible);
        lbl_4.setVisible(visible);

        lbl_1.setManaged(visible);
        lbl_2.setManaged(visible);
        lbl_3.setManaged(visible);
        lbl_4.setManaged(visible);
    }


    @FXML
    public String CD_search(ActionEvent event) throws IOException {
        // get the customerID from tfCD_customerID
        customerID = tfCD_customerID.getText().trim();

        if (customerID.isEmpty()) {
            System.out.println("CustomerID cannot be empty.");
            return null;
        }

        if (customerID.equals("/all")) {
            CD_setAllCustomerDetails();
            return "hello";
        }

        if (CustomerDatabaseHandler.validateCustomerID(customerID)) {
            setCustomerLabelsVisible(true);
            System.out.println("Customer exists");
            CD_setCustomerDetails(customerID);
            return customerID;
        } else {
            System.out.println("Customer does not exist");
            return null;
        }
    }

    @FXML
    void CD_setCustomerDetails(String customerID) {
        // set lblCD_name
        String CD_first_name = CustomerDatabaseHandler.getFirstNamebyID(customerID);
        String CD_last_name = CustomerDatabaseHandler.getLastNamebyID(customerID);
        String CD_full_name = CD_first_name + " " + CD_last_name;
        lblCD_name.setText(CD_full_name);

        // set lblCD_customerID
        lblCD_customerID.setText(customerID);

        // set lblCD_email
        String CD_email = CustomerDatabaseHandler.getCustomerEmail(customerID);
        lblCD_email.setText(CD_email);

        // set lblCD_phoneNumber
        String CD_phoneNumber = CustomerDatabaseHandler.getCustomerPhoneNumber(customerID);
        lblCD_phoneNumber.setText(CD_phoneNumber);

        // set lblCD_totalSpent
        String CD_totalSpent = CustomerDatabaseHandler.getCustomerTotalSpent(customerID);
        if (CD_totalSpent == null) {
            lblCD_totalSpent.setText("0.00");
        } else {
            lblCD_totalSpent.setText(CD_totalSpent);
        }

        tfCD_customerID.clear();
        tfCD_firstName.clear();
        tfCD_lastName.clear();
        tfCD_phoneNumber.clear();

        tableCD.setPrefHeight(319);
        tableCD.setPrefWidth(482);
        tableCD.setLayoutX(246);
        tableCD.setLayoutY(202);

        List<OrderRow> orderData = CustomerDatabaseHandler.getOrdersByCustomerID(customerID);
        ObservableList<OrderRow> data = FXCollections.observableArrayList(orderData);
        tableCD.setItems(data);

    }

    @FXML
    void CD_setAllCustomerDetails() {
        // Hide all labels
        lblCD_name.setVisible(false);
        lblCD_customerID.setVisible(false);
        lblCD_email.setVisible(false);
        lblCD_phoneNumber.setVisible(false);
        lblCD_totalSpent.setVisible(false);
        lbl_1.setVisible(false);
        lbl_2.setVisible(false);
        lbl_3.setVisible(false);
        lbl_4.setVisible(false);

        // Clear text fields
        tfCD_customerID.clear();
        tfCD_firstName.clear();
        tfCD_lastName.clear();
        tfCD_phoneNumber.clear();

        // Load table data
        List<OrderRow> orderData = CustomerDatabaseHandler.getAllCustomerOrders();
        ObservableList<OrderRow> data = FXCollections.observableArrayList(orderData);
        tableCD.setItems(data);

        // Make the table take up more space
        tableCD.setPrefHeight(502);
        tableCD.setPrefWidth(795);
        tableCD.setLayoutX(246);
        tableCD.setLayoutY(19);
    }

    @FXML
    void CD_delete() throws IOException {
        // Get the customer ID from the text field
        String customerID = tfCD_customerID.getText().trim();

        // Step 2: Confirm it's not empty
        if (customerID.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Customer ID is required.");
            return;
        }

        // confirmation dialog
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Are you sure you want to delete this customer?");
        confirmAlert.setContentText("This will also delete all related orders, wallet, and data.");

        if (confirmAlert.showAndWait().get() != ButtonType.OK) {
            return;
        }

        // Call the database handler to delete
        boolean success = CustomerDatabaseHandler.deleteCustomerByID(customerID);

        // Step 5: Give feedback
        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Customer and related data deleted successfully.");
            tfCD_customerID.clear();
        } else {
            showAlert(Alert.AlertType.ERROR, "Failed", "Customer does not exist.");
        }
    }

// --------------------------------------------------------------------------------------------------- //

    // private void setOwnerLabelsVisible(boolean visible) {
    //     lblOD_businessName.setVisible(visible);
    //     lblOD_businessOwnerEmail.setVisible(visible);
    //     lblOD_businessOwnerID.setVisible(visible);
    //     lblOD_restaurantID.setVisible(visible);
    //     lblOD_businessOwnerName.setVisible(visible);
    //     lblOD_totalEarned.setVisible(visible);
    //     lbl_5.setVisible(visible);
    //     lbl_6.setVisible(visible);
    //     lbl_7.setVisible(visible);
    //     lbl_8.setVisible(visible);

    //     lblOD_businessName.setManaged(visible);
    //     lblOD_businessOwnerEmail.setManaged(visible);
    //     lblOD_businessOwnerID.setManaged(visible);
    //     lblOD_restaurantID.setManaged(visible);
    //     lblOD_businessOwnerName.setManaged(visible);
    //     lblOD_totalEarned.setManaged(visible);
    //     lbl_5.setManaged(visible);
    //     lbl_6.setManaged(visible);
    //     lbl_7.setManaged(visible);
    //     lbl_8.setManaged(visible);
    // }

    // @FXML
    // public String OD_search(ActionEvent event) throws IOException {
    //     // get the tfOD_restaurantID
    //     restaurantID = tfOD_restaurantID.getText().trim();
    //     System.out.println(restaurantID);

    //     if (restaurantID.isEmpty()) {
    //         System.out.println("restaurantID cannot be empty.");
    //         return null;
    //     }

    //     if (restaurantID.equals("/all")) {
    //         CD_setAllOwnerDetails();
    //         return "hello";
    //     }

    //     if (CustomerDatabaseHandler.validateRestaurantID(restaurantID)) {
    //         System.out.println("Restaurant exists");
    //         OD_setBusinessOwnerDetails(restaurantID);
    //         setOwnerLabelsVisible(true);
    //         return customerID;
    //     } else {
    //         System.out.println("Restaurant does not exist");
    //         return null;
    //     }
    // }

    // @FXML
    // void OD_setBusinessOwnerDetails(String restaurantID) {
    //     // set lblOD_businessOwnerName
    //     String OD_first_name = CustomerDatabaseHandler.getBusinessOwnerFirstNameByID(restaurantID);
    //     String OD_last_name = CustomerDatabaseHandler.getBusinessOwnerLastNameByID(restaurantID);
    //     String OD_full_name = OD_first_name + " " + OD_last_name;

    //     lblOD_businessOwnerName.setText(OD_full_name);

    //     // set lblOD_restaurantID
    //     lblOD_restaurantID.setText(restaurantID);

    //     // set lblOD_businessName
    //     String OD_businessName = CustomerDatabaseHandler.getBusinessName(restaurantID);
    //     lblOD_businessName.setText(OD_businessName);

    //     // set lblOD_businessOwnerID
    //     String OD_businessOwnerID = CustomerDatabaseHandler.getbusinessOwnerID(restaurantID);
    //     lblOD_businessOwnerID.setText(OD_businessOwnerID);

    //     // set lblOD_businessOwnerEmail 
    //     String OD_email = CustomerDatabaseHandler.getBusinessOwnerEmail(OD_businessOwnerID);
    //     lblOD_businessOwnerEmail.setText(OD_email);

    //     // set lblOD_totalEarned
    //     String OD_totalSpent = CustomerDatabaseHandler.getRestaurantEarnings(restaurantID);
    //     if (OD_totalSpent == null) {
    //         lblOD_totalEarned.setText("0.00");
    //     } else {
    //         lblOD_totalEarned.setText(OD_totalSpent);
    //     }

    //     tfOD_restaurantID.clear();
    //     tfOD_businessOwnerID.clear();
    //     tfOD_firstName.clear();
    //     tfOD_lastName.clear();

    //     List<OrderRow> orderData = CustomerDatabaseHandler.getOrdersByRestaurantID(restaurantID);
    //     ObservableList<OrderRow> data = FXCollections.observableArrayList(orderData);
    //     tablePD.setItems(data);

    //     tableCD.setPrefHeight(319);
    //     tableCD.setPrefWidth(482);
    //     tableCD.setLayoutX(246);
    //     tableCD.setLayoutY(202);

    // }

    // @FXML
    // void CD_setAllOwnerDetails() {
    //     // Hide all labels
    //     setOwnerLabelsVisible(false);

    //     // Clear text fields
    //     tfCD_customerID.clear();
    //     tfCD_firstName.clear();
    //     tfCD_lastName.clear();
    //     tfCD_phoneNumber.clear();

    //     // Load table data
    //     List<OrderRow> orderData = CustomerDatabaseHandler.getAllOwnerOrders();
    //     ObservableList<OrderRow> data = FXCollections.observableArrayList(orderData);
    //     tableCD.setItems(data);

    //     // Make the table take up more space
    //     tablePD.setPrefHeight(502);
    //     tablePD.setPrefWidth(795);
    //     tablePD.setLayoutX(246);
    //     tablePD.setLayoutY(19);
    // }

    private void setOwnerLabelsVisible(boolean visible) {
        lblOD_businessName.setVisible(visible);
        lblOD_businessOwnerEmail.setVisible(visible);
        lblOD_businessOwnerID.setVisible(visible);
        lblOD_restaurantID.setVisible(visible);
        lblOD_businessOwnerName.setVisible(visible);
        lblOD_totalEarned.setVisible(visible);
        lbl_5.setVisible(visible);
        lbl_6.setVisible(visible);
        lbl_7.setVisible(visible);
        lbl_8.setVisible(visible);

        lblOD_businessName.setManaged(visible);
        lblOD_businessOwnerEmail.setManaged(visible);
        lblOD_businessOwnerID.setManaged(visible);
        lblOD_restaurantID.setManaged(visible);
        lblOD_businessOwnerName.setManaged(visible);
        lblOD_totalEarned.setManaged(visible);
        lbl_5.setManaged(visible);
        lbl_6.setManaged(visible);
        lbl_7.setManaged(visible);
        lbl_8.setManaged(visible);
    }

    @FXML
    public String OD_search(ActionEvent event) throws IOException {
        // get the tfOD_restaurantID
        restaurantID = tfOD_restaurantID.getText().trim();
        System.out.println(restaurantID);

        if (restaurantID.isEmpty()) {
            System.out.println("restaurantID cannot be empty.");
            return null;
        }

        if (restaurantID.equals("/all")) {
            OD_setAllOwnerDetails(); // Hides labels here
            return "hello";
        }

        if (CustomerDatabaseHandler.validateRestaurantID(restaurantID)) {
            System.out.println("Restaurant exists");
            OD_setBusinessOwnerDetails(restaurantID);
            setOwnerLabelsVisible(true); // Show labels for a specific ID
            return customerID;
        } else {
            System.out.println("Restaurant does not exist");
            return null;
        }
    }

    @FXML
    void OD_setBusinessOwnerDetails(String restaurantID) {
        // Make labels visible again
        setOwnerLabelsVisible(true);

        // Reset table layout to original size
        tablePD.setPrefHeight(319);
        tablePD.setPrefWidth(482);
        tablePD.setLayoutX(246);
        tablePD.setLayoutY(202);

        // Set owner info labels
        String OD_first_name = CustomerDatabaseHandler.getBusinessOwnerFirstNameByID(restaurantID);
        String OD_last_name = CustomerDatabaseHandler.getBusinessOwnerLastNameByID(restaurantID);
        String OD_full_name = OD_first_name + " " + OD_last_name;
        lblOD_businessOwnerName.setText(OD_full_name);

        lblOD_restaurantID.setText(restaurantID);

        String OD_businessName = CustomerDatabaseHandler.getBusinessName(restaurantID);
        lblOD_businessName.setText(OD_businessName);

        String OD_businessOwnerID = CustomerDatabaseHandler.getbusinessOwnerID(restaurantID);
        lblOD_businessOwnerID.setText(OD_businessOwnerID);

        String OD_email = CustomerDatabaseHandler.getBusinessOwnerEmail(OD_businessOwnerID);
        lblOD_businessOwnerEmail.setText(OD_email);

        String OD_totalSpent = CustomerDatabaseHandler.getRestaurantEarnings(restaurantID);
        lblOD_totalEarned.setText((OD_totalSpent == null) ? "0.00" : OD_totalSpent);

        // Clear search fields
        tfOD_restaurantID.clear();
        tfOD_businessOwnerID.clear();
        tfOD_firstName.clear();
        tfOD_lastName.clear();

        // Update table
        List<OrderRow> orderData = CustomerDatabaseHandler.getOrdersByRestaurantID(restaurantID);
        ObservableList<OrderRow> data = FXCollections.observableArrayList(orderData);
        tablePD.setItems(data);
    }


    @FXML
    void OD_setAllOwnerDetails() {
        // Hide all labels
        setOwnerLabelsVisible(false);

        // Clear text fields
        tfCD_customerID.clear();
        tfCD_firstName.clear();
        tfCD_lastName.clear();
        tfCD_phoneNumber.clear();

        // Load table data
        List<OrderRow> orderData = CustomerDatabaseHandler.getAllOwnerOrders();
        ObservableList<OrderRow> data = FXCollections.observableArrayList(orderData);
        tablePD.setItems(data);

        // Make the table take up more space
        tablePD.setPrefHeight(502);
        tablePD.setPrefWidth(795);
        tablePD.setLayoutX(246);
        tablePD.setLayoutY(19);
    }


    @FXML
    void OD_delete() throws IOException {
        // Get the restaurant ID from the text field
        String restaurantID = tfOD_restaurantID.getText();

        // Confirm it's not empty
        if (restaurantID == null || restaurantID.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Restaurant ID is required.");
            return;
        }

        // Optional confirmation dialog
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Are you sure you want to delete this restaurant?");
        confirmAlert.setContentText("This will also delete all related products, business owner, and wallet.");
        
        if (confirmAlert.showAndWait().get() != ButtonType.OK) {
            return;
        }

        // all the database handler to delete
        boolean success = CustomerDatabaseHandler.deleteRestaurantByID(restaurantID);

        // Give feedback
        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Restaurant and related data deleted successfully.");
            tfOD_restaurantID.clear();
        } else {
            showAlert(Alert.AlertType.ERROR, "Failed", "Restaurant Does Not Exist.");
        }
    }

// --------------------------------------------------------------------------------------------------- //

    @FXML
    public void loadData(ActionEvent event) throws IOException {
        // get the restaurantID from the tfPD_restaurantID

        restaurantID = tfPD_restaurantID.getText().trim();
        
        if (restaurantID.equals("/all")) {
            loadAllData(event);
            return;
        }

        cardGrid.getChildren().clear();
        cardGrid.getRowConstraints().clear();
        cardGrid.getColumnConstraints().clear();

        int columns = 2;
        int col = 0;
        int rows = 0;

        try {
            List<ProductItem> productItems = CustomerDatabaseHandler.getAllProductsInRestaurant(restaurantID);
            for (ProductItem product : productItems) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Customer/FXML/ProductCard.fxml"));
                AnchorPane card = fxmlLoader.load();

                ProductCardController controller = fxmlLoader.getController();
                controller.setData(product.getProductID(), product.getProductName(), product.getProductPrice(), product.getProductDescription(), product.getProductImagePath());

                cardGrid.add(card, col, rows);
                col++;
                if (col == columns) {
                    col = 0;
                    rows++;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void loadAllData(ActionEvent event) throws IOException {

        restaurantID = "all";

        cardGrid.getChildren().clear();
        cardGrid.getRowConstraints().clear();
        cardGrid.getColumnConstraints().clear();

        int columns = 2;
        int col = 0;
        int rows = 0;

        try {
            List<ProductItem> productItems = CustomerDatabaseHandler.getAllProducts();
            for (ProductItem product : productItems) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Customer/FXML/ProductCard.fxml"));
                AnchorPane card = fxmlLoader.load();

                ProductCardController controller = fxmlLoader.getController();
                controller.setData(product.getProductID(), product.getProductName(), product.getProductPrice(), product.getProductDescription(), product.getProductImagePath());

                cardGrid.add(card, col, rows);
                col++;
                if (col == columns) {
                    col = 0;
                    rows++;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void PD_delete() throws IOException {
        // Get the product ID from the text field
        String productID = tfPD_productID.getText().trim();
        String restaurantID = tfPD_restaurantID.getText().trim();

        // Confirm it's not empty
        if (productID == null || productID.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Food name is required.");
            return;
        }

        if (restaurantID == null || restaurantID.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Restaurant ID is required.");
            return;
        }

        // Optional confirmation dialog
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Are you sure you want to delete this product?");
        confirmAlert.setContentText("This will permanently remove the product from the database.");

        if (confirmAlert.showAndWait().get() != ButtonType.OK) {
            return;
        }

        // Call the database handler to delete the product
        boolean success = CustomerDatabaseHandler.deleteProductByID(productID, restaurantID);

        // Give feedback
        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Product deleted successfully.");
            tfPD_productID.clear();
        } else {
            showAlert(Alert.AlertType.ERROR, "Failed", "Product does not exist.");
        }
    }
}