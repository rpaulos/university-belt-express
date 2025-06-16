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

public class CardController {

    @FXML
    private Label lbl_restaurantName;

    @FXML
    private Label lbl_restaurantLoc;

    @FXML
    private Label lbl_price;

    @FXML
    private ImageView img_header;

    @FXML
    private Button btn_showProducts;

    private String restaurantID;

    @FXML
    private void showProductsHandler(ActionEvent event) throws IOException {
        CustomerSession.setSelectedRestaurantID(restaurantID);
        SwitchScene.switchScene(event, "/Customer/FXML/RestaurantProducts.fxml");
    }

    public void setData(String name, String address, String headerPath, String restaurantID, String priceRange) {
        this.restaurantID = restaurantID;

        // Set the restaurant name
        lbl_restaurantName.setText(name);
        String restaurantName = CustomerDatabaseHandler.getRestaurantName(restaurantID);

        // Set the restaurant address
        lbl_restaurantLoc.setText(address);
        
        // Set the restaurant header image
        String imagePath = "C:/Users/Rae/Desktop/FoodPanda/FoodPanda/src/User Interface/Restaurant Header/" + restaurantName + ".png";
        File imageFile = new File(imagePath);
        Image headerImage;

        if (imageFile.exists()) {
            headerImage = new Image(imageFile.toURI().toString());
        } else {
            // Fallback image path
            headerImage = new Image("file:C:/Users/Rae/Desktop/FoodPanda/FoodPanda/src/User Interface/Restaurant Header/" + restaurantName + ".png");

            // If the fallback image also does not exist, use a default image
            if (headerImage.isError()) {
                headerImage = new Image("file:C:/Users/Rae/Desktop/FoodPanda/FoodPanda/src/User Interface/Restaurant Header/default_header.png");
            }
        }
        
        img_header.setImage(headerImage);

        // Set the price label to a placeholder value
        lbl_price.setText(priceRange);
    }
}
