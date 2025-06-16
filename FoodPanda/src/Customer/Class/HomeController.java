package Customer.Class;

import Customer.CustomerDatabaseHandler;
import Customer.CustomerSession;
import java.io.IOException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import java.util.Collections;
import javafx.scene.control.Label;

import Customer.SwitchScene;
import java.awt.event.ActionEvent;

public class HomeController {

    @FXML
    private Button btn_Search;

    @FXML
    private Button btn_account;

    @FXML
    private Button btn_grocery;

    @FXML
    private Button btn_cart;

    @FXML
    private Button btn_like;

    @FXML
    private GridPane cardGrid;

    @FXML
    private Label lbl_location;

    @FXML
    private ComboBox<String> cb_priceRange;

    private Stage stage;
    private Scene scene; 
    private Parent root;

    @FXML
    public void toProfilePageHandler(ActionEvent event) throws IOException{
        SwitchScene.switchScene(event, "/Customer/FXML/Profile.fxml");
    }

    public void initialize() {
        setLocation();
        cb_priceRange.getItems().addAll("₱", "₱₱", "₱₱₱");

        cardGrid.getChildren().clear();
        cardGrid.getRowConstraints().clear();
        cardGrid.getColumnConstraints().clear();

        int columns = 1;
        int col = 0;
        int row = 0;

        try {
            List<RestaurantItem> restaurantList = CustomerDatabaseHandler.getRestaurantItems();
            //Collections.shuffle(restaurantList);

            for (RestaurantItem restaurant : restaurantList) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Customer/FXML/Card.fxml"));
                AnchorPane card = fxmlLoader.load();

                CardController controller = fxmlLoader.getController();

                System.out.println("Price Range: " + restaurant.getPriceRange());

                controller.setData(restaurant.getName(), restaurant.getAddress(), restaurant.getHeaderPath(), restaurant.getRestaurantID(), restaurant.getPriceRange());

                cardGrid.add(card, col, row);
                col++;
                if (col == columns) {
                    col = 0;
                    row++;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void filteredRestaurants(ActionEvent event) {

        String selectedPriceRange = cb_priceRange.getValue();

        cardGrid.getChildren().clear();
        cardGrid.getRowConstraints().clear();
        cardGrid.getColumnConstraints().clear();

        int columns = 1;
        int col = 0;
        int row = 0;

        try {
            List<RestaurantItem> restaurantList = CustomerDatabaseHandler.getFilteredRestaurants(selectedPriceRange);
            //Collections.shuffle(restaurantList);

            for (RestaurantItem restaurant : restaurantList) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Customer/FXML/Card.fxml"));
                AnchorPane card = fxmlLoader.load();

                CardController controller = fxmlLoader.getController();

                //System.out.println("Price Range: " + restaurant.getPriceRange());

                controller.setData(restaurant.getName(), restaurant.getAddress(), restaurant.getHeaderPath(), restaurant.getRestaurantID(), restaurant.getPriceRange());

                cardGrid.add(card, col, row);
                col++;
                if (col == columns) {
                    col = 0;
                    row++;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    
    }

    public void setLocation() {
        String customerLocationID = CustomerDatabaseHandler.getCustomerLocationID(CustomerSession.getEmail());
        String address = CustomerDatabaseHandler.getAddress(customerLocationID);
        
        CustomerSession.setAddress(address);
        lbl_location.setText(address);
    }

    public void toCartHandler(ActionEvent event) throws IOException{
        SwitchScene.switchScene(event, "/Customer/FXML/Cart.fxml");
    }

    public void toComingSoon(ActionEvent event) throws IOException{
        SwitchScene.switchScene(event, "/Customer/FXML/ComingSoon.fxml");
    }
}

