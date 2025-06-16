package Customer.Class;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.io.IOException;
import Customer.SwitchScene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import java.lang.classfile.Label;
import javafx.scene.text.Text;
import javafx.scene.control.TextArea;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.control.Button;

import Customer.CustomerSession;
import Customer.CustomerDatabaseHandler;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AddProductController {

    @FXML
    private Button btn_BuyNow;

    @FXML
    private Button btn_account;

    @FXML
    private Button btn_add;

    @FXML
    private Button btn_addToBasket;

    @FXML
    private Button btn_back;

    @FXML
    private Button btn_cart;

    @FXML
    private Button btn_food;

    @FXML
    private Button btn_grocery;

    @FXML
    private Button btn_like;

    @FXML
    private Button btn_search;

    @FXML
    private Button btn_subtract;

    @FXML
    private ImageView img_product_header;

    @FXML
    private Label lbl_price;

    @FXML
    private Label lbl_product_name;

    @FXML
    private TextField tf_quantity;

    @FXML
    private Text txt_desc;

    public static String myProductID;
    //public static String restaurantName;

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void backToRestaurant(ActionEvent event) throws IOException {
        SwitchScene.switchScene(event, "/Customer/FXML/RestaurantProducts.fxml");
    }

    public void initialize() {
        setAddProductData();
    }

    public void setAddProductData() {
        String productID = myProductID;
        String restaurantID = CustomerSession.getSelectedRestaurantID();
        
        String productName = CustomerDatabaseHandler.getProductName(productID);
        String productPrice = CustomerDatabaseHandler.getProductPrice(productID);
        String productDescription = CustomerDatabaseHandler.getProductDescription(productID);
        String productQuantity = CustomerDatabaseHandler.getProductQuantity(productID);

        String restaurantName = CustomerDatabaseHandler.getRestaurantName(restaurantID);

        // Set the product header image
        String headerPath = "C:/Users/Rae/Desktop/FoodPanda/FoodPanda/src/User Interface/Restaurant Product Header/" + restaurantName + "/" + productName + ".png";
        
        System.out.println(restaurantName);
        System.out.println(productName);
        
        File imageFile = new File(headerPath);
        Image headerImage;

        if (imageFile.exists()) {
            headerImage = new Image(imageFile.toURI().toString());
        } else {
            // Fallback image path
            headerImage = new Image("file:C:/Users/Rae/Desktop/FoodPanda/FoodPanda/src/User Interface/Restaurant Product Header/default_product_header.png");
        }
        
        img_product_header.setImage(headerImage);

        // Set the product details in the UI
        lbl_product_name.setText(productName);
        lbl_price.setText(productPrice);
        txt_desc.setText(productDescription);
        //tf_quantity.setText(productQuantity);
    }

    
    @FXML
    void buyNow(ActionEvent event) throws IOException {
        // save the productID to the session
        CustomerSession.setSelectedProductID(myProductID);

        // save quantity to the session
        String quantityText = tf_quantity.getText();
        CustomerSession.setQuantity(quantityText);

        SwitchScene.switchScene(event, "/Customer/FXML/Checkout.fxml");
    }

    @FXML
    void addToCart(ActionEvent event) throws IOException{
        // get the customerID, productID, restaurantID
        String customerID = CustomerSession.getCustomerID();
        String productID = myProductID;
        String restaurantID = CustomerSession.getSelectedRestaurantID();
        String existingRestaurantID = CustomerDatabaseHandler.getRestaurantIDinCart(customerID);

        if (existingRestaurantID != null && !existingRestaurantID.equals(restaurantID)) {
            // Show error message if the restaurant ID in the cart does not match the selected restaurant ID
            //System.out.println("Error: You can only add products from one restaurant at a time.");
            showAlert(Alert.AlertType.WARNING, "Add Product Unsuccessful", "You can only add products from one restaurant at a time.");

            return;
        }

        // get the quanity from the text field
        String quantityText = tf_quantity.getText();

        // get the quantity of the product from the database
        String db_quantity = CustomerDatabaseHandler.getProductQuantity(myProductID);



        // if tf_quantity is empty, set it to 1
        if (quantityText == null || quantityText.trim().isEmpty()) {
            tf_quantity.setText("1");
        }

        if (!String.valueOf(quantityText).matches("\\d+")) {
            //System.out.println("Error: Quantity must be a number.");
            showAlert(Alert.AlertType.WARNING, "Add Product Unsuccessful", "Quantity must be a number.");
            tf_quantity.setText("1");
            return;
        }

        // if tf_quantity is more than db_quantity, error message
        if (Integer.parseInt(quantityText) > Integer.parseInt(db_quantity)) {
            // Show error message
            //System.out.println("Error: Quantity exceeds available stock.");
            showAlert(Alert.AlertType.WARNING, "Add Product Unsuccessful", "Quantity exceeds available stock.");
            return;
        }

        quantityText = tf_quantity.getText();

        // check if product is already in the cart
        boolean isProductInCart = CustomerDatabaseHandler.isProductInCart(customerID, productID, restaurantID);

        // if product is already in the cart, update the quantity
        if (isProductInCart) {
            int maxQuantity = CustomerDatabaseHandler.getMaxQuantityInCart(customerID, productID, restaurantID);
            int quantityInCart = CustomerDatabaseHandler.getCartQuantity(customerID, productID, restaurantID);

            if (quantityInCart + Integer.parseInt(quantityText) > maxQuantity) {
                //System.out.println("Error: Quantity exceeds available stock in cart.");
                showAlert(Alert.AlertType.WARNING, "Add Product Unsuccessful", "Quantity exceeds available stock");
            } else {
                CustomerDatabaseHandler.addCartQuantity(customerID, productID, restaurantID, quantityText);
            }
        } else {
            // Add product to cart
            CustomerDatabaseHandler.addToCart(customerID, productID, restaurantID, quantityText);
            //System.out.println("Product added to cart successfully.");
            showAlert(Alert.AlertType.INFORMATION, "Add Product Successful", "Product added to cart successfully.");

        }
    }

    @FXML
    void addQuantity(ActionEvent event) throws IOException {
        String input = tf_quantity.getText();

        // Validate input before parsing
        if (!input.matches("\\d+")) {
            //System.out.println("Error: Quantity must be a number.");
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Quantity must be a number.");
            tf_quantity.setText("1");
            return;
        }

        int currentQuantity = Integer.parseInt(input);

        if (currentQuantity < 1) {
            //System.out.println("Error: Quantity must be at least 1.");
            showAlert(Alert.AlertType.ERROR, "Invalid Quantity", "Quantity must be at least 1.");
            tf_quantity.setText("1");
            return;
        }

        currentQuantity++;
        tf_quantity.setText(String.valueOf(currentQuantity));
    }

    @FXML
    void deductQuantity(ActionEvent event) throws IOException {
        String input = tf_quantity.getText();

        // Validate input before parsing
        if (!input.matches("\\d+")) {
            //System.out.println("Error: Quantity must be a number.");
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Quantity must be a number.");
            tf_quantity.setText("1");
            return;
        }

        int currentQuantity = Integer.parseInt(input);

        if (currentQuantity < 1) {
            //System.out.println("Error: Quantity must be at least 1.");
            showAlert(Alert.AlertType.ERROR, "Invalid Quantity", "Quantity must be at least 1.");
            tf_quantity.setText("1");
            return;
        }

        currentQuantity--;
        tf_quantity.setText(String.valueOf(currentQuantity));
    }

    @FXML
    public void toComingSoon(ActionEvent event) throws IOException {
        SwitchScene.switchScene(event, "/Customer/FXML/ComingSoon.fxml");
    }

    @FXML
    public void toCart(ActionEvent event) throws IOException {
        SwitchScene.switchScene(event, "/Customer/FXML/Cart.fxml");
    }

    @FXML
    public void toHome(ActionEvent event) throws IOException {
        SwitchScene.switchScene(event, "/Customer/FXML/Home.fxml");
    }

    @FXML
    public void toProfile(ActionEvent event) throws IOException {
        SwitchScene.switchScene(event, "/Customer/FXML/Profile.fxml");
    }

}