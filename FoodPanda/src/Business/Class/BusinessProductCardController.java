package Business.Class;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class BusinessProductCardController {

    @FXML
    private ImageView img_product;

    @FXML
    private Text lbl_productDesc;

    @FXML
    private Label lbl_productName;

    @FXML
    private Label lbl_productPrice;

    public void setData(String name, double price, String desc, String imagePath) {
    lbl_productName.setText(name);
    lbl_productPrice.setText(String.format("₱ %.2f", price));
    lbl_productDesc.setText(desc);
    
    if (imagePath != null && !imagePath.isEmpty()) {
        Image image = new Image(new File(imagePath).toURI().toString());
        img_product.setImage(image);
    } else {
        // Set default image or clear
        img_product.setImage(null);
    }
}

}
