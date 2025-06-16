package Customer.Class;

import Customer.CustomerDatabaseHandler;
import Customer.CustomerSession;
import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import Customer.SwitchScene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class CheckoutCardController {

    @FXML
    private ImageView img_product_image;

    @FXML
    private Text lbl_productDescription;

    @FXML
    private Label lbl_productName;

    @FXML
    private Label lbl_quantity;

    private String productID;
    private int quantity;
    private String customerID = CustomerSession.getCustomerID();

    public void setData(String productID, String productName, String productPrice, String productDesc, String productQuantity) {
        this.productID = productID;
        this.quantity = Integer.parseInt(productQuantity);
        this.customerID = CustomerSession.getCustomerID();

        lbl_productName.setText(productName);
        lbl_productDescription.setText(productDesc);
        lbl_quantity.setText(productQuantity + "×");

        String restaurantID = CustomerSession.getSelectedRestaurantID();
        String restaurantName = CustomerDatabaseHandler.getRestaurantName(restaurantID);

        String imagePath = "C:/Users/Rae/Desktop/FoodPanda/FoodPanda/src/User Interface/Restaurant Products/" + restaurantName + "/" + productName + ".png";
        File imageFile = new File(imagePath);

        System.out.println("Product ID: " + productID);
        System.out.println("Restaurant Name: " + restaurantName);

        Image productImage;
        if (imageFile.exists()) {
            productImage = new Image(imageFile.toURI().toString());
        } else {
            // Fallback image path
            productImage = new Image("file:C:/Users/Rae/Desktop/FoodPanda/FoodPanda/src/User Interface/Restaurant Products/default_product.png");
        }

        img_product_image.setImage(productImage);
    }
    
}
