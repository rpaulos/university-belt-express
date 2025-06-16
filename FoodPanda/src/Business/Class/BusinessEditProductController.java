package Business.Class;

import java.io.IOException;

import javax.swing.Action;

import Business.BusinessDatabaseHandler;
import Business.BusinessSession;
import Business.SwitchScene;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class BusinessEditProductController {

    @FXML
    private Button btn_deleteProduct;

    @FXML
    private Button btn_returntoProfile;

    @FXML
    private Button btn_updateProduct;

    @FXML
    private TableColumn<Product, String> col_productId;

    @FXML
    private TableColumn<Product, String> col_productName;

    @FXML
    private TableColumn<Product, String> col_productDescription;

    @FXML
    private TableView<Product> tbl_productView;

    @FXML
    private TextField tf_eproductID;

    @FXML
    private TextField tf_eproductName;

    @FXML
    private TextArea tfa_eproductDescription;

    private final ObservableList<Product> productList = FXCollections.observableArrayList();

    @FXML
    public void toReturntoProfilePageAddHandler(ActionEvent event) throws IOException{
        SwitchScene.switchScene(event, "/Business/FXML/BusinessProfile.fxml");
    }

    @FXML
    public void initialize() {
        col_productId.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        col_productDescription.setCellValueFactory(new PropertyValueFactory<>("productDescription"));

        tbl_productView.setItems(productList);

        tbl_productView.setOnMouseClicked(event -> {
            Product selected = tbl_productView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                tf_eproductID.setText(selected.getId());
                tf_eproductName.setText(selected.getProductName());
                tfa_eproductDescription.setText(selected.getProductDescription());
                tf_eproductID.setEditable(false); // product ID should not be edited
            }
        });

        loadProducts();
    }

    private void loadProducts() {
        productList.clear();
        String restaurantId = BusinessSession.getRestaurantID();
        if (restaurantId != null) {
            productList.addAll(BusinessDatabaseHandler.getProductsByRestaurantID(restaurantId));
        }
    }

    @FXML
    private void handleUpdateProduct(ActionEvent event) {
        String id = tf_eproductID.getText();
        String productName = tf_eproductName.getText();
        String productDescription = tfa_eproductDescription.getText();

        if (id.isEmpty() || productName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a product and enter a new name.");
            return;
        }

        boolean success = BusinessDatabaseHandler.updateProductName(id, productName, productDescription);
        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Product name and description updated.");
            loadProducts(); // Refresh table
            clearFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Update failed.");
        }
    }

    @FXML
    private void handleDeleteProduct(ActionEvent event) {
        String id = tf_eproductID.getText();

        if (id.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a product to delete.");
            return;
        }

        boolean success = BusinessDatabaseHandler.deleteProductByID(id);
        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Product deleted.");
            loadProducts();
            clearFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Delete failed.");
        }
    }

    private void clearFields() {
        tf_eproductID.clear();
        tf_eproductName.clear();
        tfa_eproductDescription.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
