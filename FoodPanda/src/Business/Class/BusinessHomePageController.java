package Business.Class;

import java.io.IOException;

import Business.SwitchScene;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.net.URL;
import java.util.ResourceBundle;
import Business.BusinessDatabaseHandler;
import Business.BusinessSession;

public class BusinessHomePageController implements Initializable{

    @FXML
    private Button btn_addProduct;

    @FXML
    private Button btn_homePage;

    @FXML
    private Button btn_homePageAccount;

    @FXML
    private Label lbl_restaurantName;

    @FXML
    private GridPane productcardGrid;

    private Stage stage;
    private Scene scene; 
    private Parent root;

    public static String restaurantID;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        restaurantID = BusinessSession.getRestaurantID();
        setRestaurantName();
        loadProducts();
    }

    private void setRestaurantName() {
        String restaurantName = BusinessDatabaseHandler.getRestaurantName(restaurantID);
        lbl_restaurantName.setText(restaurantName);
        System.out.println("Restaurant Name: " + restaurantName);
        System.out.println("Restaurant ID: " + restaurantID);
    }

    private void loadProducts() {
        productcardGrid.getChildren().clear();
        productcardGrid.getRowConstraints().clear();
        productcardGrid.getColumnConstraints().clear();

        int columns = 1; // You can change this for multiple columns layout
        int col = 0;
        int row = 0;

        try {
            var products = BusinessDatabaseHandler.getProductsByRestaurantID(restaurantID);
            for (var product : products) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Business/FXML/BusinessProductCard.fxml"));
                AnchorPane card = fxmlLoader.load();

                BusinessProductCardController controller = fxmlLoader.getController();
                controller.setData(product.getProductName(), product.getProductPrice(), product.getProductDescription(), product.getProductImagePath());

                productcardGrid.add(card, col, row);

                col++;
                if (col == columns) {
                    col = 0;
                    row++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR, "Failed to load products.");
            alert.showAndWait();
        }
    }

    @FXML
    public void toAddAProductHandler(ActionEvent event) throws IOException{
        SwitchScene.switchScene(event, "/Business/FXML/BusinessAddProduct.fxml");
    }

    @FXML
    public void toMoveToProfileHandler(ActionEvent event) throws IOException{
        SwitchScene.switchScene(event, "/Business/FXML/BusinessProfile.fxml");
    }
    
}