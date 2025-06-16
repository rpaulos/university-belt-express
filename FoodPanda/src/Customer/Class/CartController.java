package Customer.Class;

import Business.SwitchScene;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.AnchorPane;
import Customer.Class.CartItem;
import java.io.IOException;
import java.util.List;
import Customer.Class.CartCardController;
import Customer.CustomerDatabaseHandler;
import Customer.CustomerSession;

public class CartController {

    @FXML
    private Button btn_back;

    @FXML
    private Button btn_payment;

    @FXML
    private GridPane cardGrid;

    @FXML
    private Label lbl_price;

    private String customerID = Customer.CustomerSession.getCustomerID();

    @FXML
    void initialize() {
        loadCartData();
    }

    @FXML
    void back(ActionEvent event) throws IOException {
        SwitchScene.switchScene(event, "/Customer/FXML/Home.fxml");
    }

    private void loadCartData() {
        String totalPrice = Customer.CustomerDatabaseHandler.getTotalPrice(customerID);
        lbl_price.setText(totalPrice);
        Customer.CustomerSession.setOrderAmount(totalPrice);

        String restaurant = CustomerDatabaseHandler.getRestaurantName(CustomerSession.getSelectedRestaurantID());

        System.out.println("Total Price: " +totalPrice);

        String customerID = Customer.CustomerSession.getCustomerID();
        
        cardGrid.getChildren().clear();
        cardGrid.getRowConstraints().clear();
        cardGrid.getColumnConstraints().clear();

        int columns = 1;
        int col = 0;
        int rows = 0;

        try {
            List<CartItem> cartItems = Customer.CustomerDatabaseHandler.getCartItems(customerID);
            for (CartItem item : cartItems) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Customer/FXML/CartCard.fxml"));
                AnchorPane card = loader.load();

                CartCardController controller = loader.getController();
                controller.setData(item.getProductID(), item.getProductName(), item.getProductPrice(), item.getProductDesc(), item.getProductQuantity());

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
    void payment(ActionEvent event) throws IOException {
        //set the total price in the session
        Customer.CustomerSession.setTotalPrice(lbl_price.getText());

        SwitchScene.switchScene(event, "/Customer/FXML/Checkout.fxml");
        System.out.println(Customer.CustomerSession.getTotalPrice());
    }

}
