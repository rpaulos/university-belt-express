package Business.Class;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import Business.BusinessDatabaseHandler;
import Business.BusinessSession;
import Business.SwitchScene;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class BusinessProfileController implements Initializable {

    @FXML
    private Button btn_businessHome;

    @FXML
    private Button btn_businessLogout;

    @FXML
    private Button btn_businessPay;

    @FXML
    private Button btn_businessPrices;

    @FXML
    private Button btn_businessProducts;

    @FXML
    private Button btn_businessStocks;

    @FXML
    private Hyperlink hplnk_helpCenter;

    @FXML
    private Hyperlink hplnk_termPolicies;

    @FXML
    private Label lbl_restaurantName;

    private Stage stage;
    private Scene scene; 
    private Parent root;

    public static String restaurantID;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        restaurantID = BusinessSession.getRestaurantID();
        setRestaurantName();
    }

    private void setRestaurantName() {
        String restaurantName = BusinessDatabaseHandler.getRestaurantName(restaurantID);
        lbl_restaurantName.setText(restaurantName);
        System.out.println("Restaurant Name: " + restaurantName);
        System.out.println("Restaurant ID: " + restaurantID);
    }

    @FXML
    public void toMoveToHomeHandler(ActionEvent event) throws IOException{
        SwitchScene.switchScene(event, "/Business/FXML/BusinessHomePage.fxml");
    }

    @FXML
    public void toMoveToEditProductHandler(ActionEvent event) throws IOException{
        SwitchScene.switchScene(event, "/Business/FXML/BusinessEditProduct.fxml");
    }
    
    @FXML
    public void toMoveToLandingPageHandler(ActionEvent event) throws IOException{
        SwitchScene.switchScene(event, "/LandingPage.fxml");
    }

    @FXML
    public void toMoveToEditStockHandler(ActionEvent event) throws IOException{
        SwitchScene.switchScene(event, "/Business/FXML/BusinessEditStock.fxml");
    }

    @FXML
    public void toMoveToEditPriceHandler(ActionEvent event) throws IOException{
        SwitchScene.switchScene(event, "/Business/FXML/BusinessEditPrice.fxml");
    }

    @FXML
    public void toGoToHelpCenterLink(ActionEvent event) throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI("https://www.foodpanda.ph/contents/contact.htm"));
    }

    @FXML
    public void toGoToTermsAndPoliciesLink(ActionEvent event) throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI("https://www.foodpanda.ph/contents/terms-and-conditions.htm"));
    }
}
