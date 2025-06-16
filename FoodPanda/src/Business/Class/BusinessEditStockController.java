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

public class BusinessEditStockController {

    @FXML
    private Button btn_sreturntoProfile;

    @FXML
    private Button btn_updateProductQuantity;

    @FXML
    private TableColumn<Product, String> col_stockproductId;

    @FXML
    private TableColumn<Product, String> col_stockproductName;

    @FXML
    private TableColumn<Product, Integer> col_productQuantity;

    @FXML
    private TableView<Product> tbl_stockView;

    @FXML
    private TextField tf_sproductID;

    @FXML
    private TextField tf_sproductName;

    @FXML
    private TextField tf_sproductQuantity;

    private final ObservableList<Product> productList = FXCollections.observableArrayList();


    @FXML
    public void toReturntoProfilePageStockHandler(ActionEvent event) throws IOException {
        SwitchScene.switchScene(event, "/Business/FXML/BusinessProfile.fxml");
    }

    @FXML
    public void initialize() {
        col_stockproductId.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_stockproductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        col_productQuantity.setCellValueFactory(new PropertyValueFactory<>("productQuantity"));

        tbl_stockView.setItems(productList);

        tbl_stockView.setOnMouseClicked(event -> {
            Product selected = tbl_stockView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                tf_sproductID.setText(selected.getId());
                tf_sproductName.setText(selected.getProductName());
                tf_sproductQuantity.setText(String.valueOf(selected.getProductQuantity()));
                tf_sproductID.setEditable(false);
                tf_sproductName.setEditable(false);
            }
        });

        loadStockProducts();
    }

    private void loadStockProducts() {
        productList.clear();
        String restaurantId = BusinessSession.getRestaurantID();
        if (restaurantId != null) {
            productList.addAll(BusinessDatabaseHandler.getProductsByRestaurantID(restaurantId));
        }
    }

    @FXML
    private void handleUpdateStock(ActionEvent event) {
        String id = tf_sproductID.getText();
        String quantityText = tf_sproductQuantity.getText();

        if (id.isEmpty() || quantityText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a product and enter a quantity.");
            return;
        }

        try {
            int newQuantity = Integer.parseInt(quantityText);

            boolean success = BusinessDatabaseHandler.updateProductQuantity(id, newQuantity);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Product quantity updated.");
                loadStockProducts();
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update product quantity.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Quantity must be a valid integer.");
        }
    }

    private void clearFields() {
        tf_sproductID.clear();
        tf_sproductName.clear();
        tf_sproductQuantity.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
