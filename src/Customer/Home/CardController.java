package Customer.Home;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CardController {

    @FXML
    private Label lbl_foodName;

    @FXML
    private Label lbl_price;

    @FXML
    private Label lbl_restaurantPlaceholder;

    public void setData(String name, String price, String restaurant) {
        lbl_foodName.setText(name);
        lbl_price.setText("₱" + price);
        lbl_restaurantPlaceholder.setText(restaurant);
    }
}
