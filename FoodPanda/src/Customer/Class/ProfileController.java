package Customer.Class;

import java.io.IOException;
import java.util.ResourceBundle;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import Customer.CustomerDatabaseHandler;
import Customer.CustomerSession;
import Customer.SwitchScene;
import java.awt.Desktop;

public class ProfileController implements Initializable {

    @FXML
    private Button btn_addresses;

    @FXML
    private Button btn_favourites;

    @FXML
    private Button btn_food;

    @FXML
    private Button btn_fpbusiness;

    @FXML
    private Button btn_grocery;

    @FXML
    private Button btn_helpcenter;

    @FXML
    private Button btn_likes;

    @FXML
    private Button btn_logout;

    @FXML
    private Button btn_orders;

    @FXML
    private Button btn_pandapay;

    @FXML
    private Button btn_search;

    @FXML
    private Button btn_settings;

    @FXML
    private Button btn_termspolicies;

    @FXML
    private Label lbl_name;

    private Stage stage;
    private Scene scene; 
    private Parent root;

    public static String userEmail;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userEmail = CustomerSession.getEmail();
        setProfile();

    }

    public void setProfile() {
        String first_name = CustomerDatabaseHandler.getFirstName(userEmail);
        String last_name = CustomerDatabaseHandler.getLastName(userEmail);
        String fullName = first_name + " " + last_name;
        lbl_name.setText(fullName);
    }

    @FXML
    public void toHomeHandler(ActionEvent event) throws IOException{
        SwitchScene.switchScene(event, "/Customer/FXML/Home.fxml");
    }

    @FXML
    public void toLandingPageHandler(ActionEvent event) throws IOException{
        CustomerSession.clearSession();
        SwitchScene.switchScene(event, "/LandingPage.fxml");
    }

    @FXML
    public void toHelpCenterHandler(ActionEvent event) throws URISyntaxException, IOException{
        Desktop.getDesktop().browse(new URI("https://mail.google.com/mail/u/0/#inbox"));
    }
    
    @FXML
    public void toBusinessHandler(ActionEvent event) throws URISyntaxException, IOException{
        Desktop.getDesktop().browse(new URI("https://partner.foodpanda.ph/s/?language=en_US&countryIsoCode=PH&utm_source=google&utm_medium=pmax&utm_campaign=sem_gen_web_ssu_ma_FP_PH_OBJ-ssu_CH-Google_SC-pmax%2Bsearch_CT-PMX_%5B_NAT_EN%5D&gad_source=1&gad_campaignid=22263028721&gbraid=0AAAAADiVv6Ouj5hFxK5CNJKt7Jn5Kq22I&gclid=CjwKCAjw6NrBBhB6EiwAvnT_rsuArTRiGvwHNl1oNbsa9hBCO_v9J_RSf9BkhqSmZTXW6Emr_eN-ZRoCUCQQAvD_BwE"));
    }

    @FXML
    public void toTermsAndPoliciesHandler(ActionEvent event) throws URISyntaxException, IOException{
        Desktop.getDesktop().browse(new URI("https://www.foodpanda.ph/contents/terms-and-conditions.htm"));
    }

    @FXML
    public void toComingSoon(ActionEvent event) throws IOException {
        SwitchScene.switchScene(event, "/Customer/FXML/ComingSoon.fxml");
    }
}