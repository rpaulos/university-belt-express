package Customer.Class;

import Customer.CustomerDatabaseHandler;
import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import Customer.SwitchScene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import Customer.CustomerSession;

public class ProductCardController {
    
    @FXML
    private ImageView img_product_image;

    @FXML
    private Label lbl_price;

    @FXML
    private Label lbl_product_name;

    @FXML
    private Text txt_desc;

    private String productID;

    public static String myProductID;

    @FXML
    void showAddProductHandler(ActionEvent event) throws IOException {
        CustomerSession.setSelectedProductID(productID);

        myProductID = productID;
        AddProductController.myProductID = productID;

        System.out.println(productID);
        SwitchScene.switchScene(event, "/Customer/FXML/AddProduct.fxml");

    }

    public void setData(String productID, String name, String price, String description, String imagePath) {
        String restaurantID = CustomerSession.getSelectedRestaurantID();
        String restaurantName = CustomerDatabaseHandler.getRestaurantName(restaurantID);
        String productName = CustomerDatabaseHandler.getProductName(productID);

        System.out.println("Product ID: " + productID);
        System.out.println("Restaurant Name: " + restaurantName);
        System.out.println("Product Name: " + productName);

        this.productID = productID;

        // Set the product name
        lbl_product_name.setText(name);

        // Set the product price
        lbl_price.setText(price);

        // Set the product description
        txt_desc.setText(description);

        // Set the product image
        File imageFile = new File(imagePath);
        //String productImagePath = "C:/Users/Rae/Desktop/FoodPanda/FoodPanda/src/User Interface/Restaurant Products/" + restaurantName + "/" + productName + ".png";
        //File imageFile = new File(productImagePath);
        Image productImage;

        if (imageFile.exists()) {
            productImage = new Image(imageFile.toURI().toString());
        } else {
            // Fallback image path
            productImage = new Image("file:C:/Users/Rae/Desktop/FoodPanda/FoodPanda/src/User Interface/Restaurant Products/default_product.png");
        }

        // For testing purposes only
        //productImage = new Image("file:C:/Users/Rae/Desktop/FoodPanda/FoodPanda/src/User Interface/Restaurant Products/default_product.png");

        img_product_image.setImage(productImage);

    }


}