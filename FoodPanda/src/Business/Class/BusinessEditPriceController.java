package Business.Class;

import java.io.IOException;

import Business.BusinessDatabaseHandler;
import Business.BusinessSession;
import Business.SwitchScene;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class BusinessEditPriceController {

    @FXML
    private Button btn_sreturntoProfile;

    @FXML
    private Button btn_updateProductPrice;

    @FXML
    private TableColumn<Product, String> col_priceproductId;

    @FXML
    private TableColumn<Product, String> col_priceproductName;

    @FXML
    private TableColumn<Product, Double> col_productPrice;

    @FXML
    private TableView<Product> tbl_priceView;

    @FXML
    private TextField tf_pproductID;

    @FXML
    private TextField tf_pproductName;

    @FXML
    private TextField tf_pproductPrice;

    private final ObservableList<Product> productList = FXCollections.observableArrayList();

    @FXML
    public void toReturntoProfilePagePriceHandler(ActionEvent event) throws IOException {
        SwitchScene.switchScene(event, "/Business/FXML/BusinessProfile.fxml");
    }

    @FXML
    public void initialize() {
        col_priceproductId.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_priceproductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        col_productPrice.setCellValueFactory(new PropertyValueFactory<>("productPrice"));

        tbl_priceView.setItems(productList);

        tbl_priceView.setOnMouseClicked(event -> {
            Product selected = tbl_priceView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                tf_pproductID.setText(selected.getId());
                tf_pproductName.setText(selected.getProductName());
                tf_pproductPrice.setText(String.valueOf(selected.getProductPrice()));
                tf_pproductID.setEditable(false);
                tf_pproductName.setEditable(false);
            }
        });

        loadPriceProducts();
    }

    private void loadPriceProducts() {
        productList.clear();
        String restaurantId = BusinessSession.getRestaurantID();
        if (restaurantId != null) {
            productList.addAll(BusinessDatabaseHandler.getProductsByRestaurantID(restaurantId));
        }
    }

    @FXML
    private void handleUpdatePrice(ActionEvent event) {
        String id = tf_pproductID.getText();
        String priceText = tf_pproductPrice.getText();

        if (id.isEmpty() || priceText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a product and enter a price.");
            return;
        }

        // Validate that the price is a number with up to 2 decimal places
        if (!priceText.matches("\\d+(\\.\\d{1,2})?")) {
            showAlert(Alert.AlertType.ERROR, "Invalid Price", "Price must be a number with up to 2 decimal places (e.g., 49.99).");
            return;
        }

        try {
            double productPrice = Double.parseDouble(priceText);

            boolean success = BusinessDatabaseHandler.updateProductPrice(id, productPrice);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Product price updated.");
                loadPriceProducts();
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update product price.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Price must be a valid number.");
        }
    }

    private void clearFields() {
        tf_pproductID.clear();
        tf_pproductName.clear();
        tf_pproductPrice.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
