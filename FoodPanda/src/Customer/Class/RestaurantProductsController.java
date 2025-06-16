package Customer.Class;

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
import java.awt.image.ImageFilter;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class RestaurantProductsController {


    @FXML
    private Button btn_Search;

    @FXML
    private Button btn_account;

    @FXML
    private Button btn_backToFood;

    @FXML
    private Button btn_cart;

    @FXML
    private Button btn_food;

    @FXML
    private Button btn_like;

    @FXML
    private Button btn_restaurant;

    @FXML
    private GridPane cardGrid;

    @FXML
    private ImageView img_restaurantLogo;

    @FXML
    private Label lbl_restaurantName;

    private Stage stage;
    private Scene scene; 
    private Parent root;

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void toProfilePageHandler(ActionEvent event) throws IOException {
        SwitchScene.switchScene(event, "/Customer/FXML/Profile.fxml");
    }

    @FXML
    public void toHomeHandler(ActionEvent event) throws IOException {
        SwitchScene.switchScene(event, "/Customer/FXML/Home.fxml");
    }

    public void initialize() {
        String restaurantID = CustomerSession.getSelectedRestaurantID();
        setRestaurantDetails();
        cardGrid.getChildren().clear();
        cardGrid.getRowConstraints().clear();
        cardGrid.getColumnConstraints().clear();

        int columns = 1;
        int col = 0;
        int rows = 0;

        try {
            List<ProductItem> productItems = CustomerDatabaseHandler.getProductItems(restaurantID);
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

    private void setRestaurantDetails() {

        String restaurantID = CustomerSession.getSelectedRestaurantID();
        //System.out.println("Selected Restaurant ID: " + restaurantID);

        // Set the restaurant name
        String restaurantName = CustomerDatabaseHandler.getRestaurantName(restaurantID);
        lbl_restaurantName.setText(restaurantName);

        // Set the logo image path
        String restaurantLogo = "C:/Users/Rae/Desktop/FoodPanda/FoodPanda/src/User Interface/Restaurant Logo/" + restaurantName + ".png";
        File imageFile = new File(restaurantLogo);

        Image restaurantLogoImage;
        if (imageFile.exists()) {
            restaurantLogoImage = new Image(imageFile.toURI().toString());
        } else {
            File defaultImage = new File("C:/Users/Rae/Desktop/FoodPanda/FoodPanda/src/User Interface/Restaurant Logo/default_logo.png");
            restaurantLogoImage = new Image(defaultImage.toURI().toString());        
        }

        img_restaurantLogo.setImage(restaurantLogoImage);
    }

    @FXML
    public void toComingSoon(ActionEvent event) throws IOException {
        SwitchScene.switchScene(event, "/Customer/FXML/ComingSoon.fxml");
    }

    @FXML
    public void toCart(ActionEvent event) throws IOException {
        SwitchScene.switchScene(event, "/Customer/FXML/Cart.fxml");
    }

}
