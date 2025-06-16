package Customer.Class;

import Business.SwitchScene;
import Customer.CustomerDatabaseHandler;
import Customer.CustomerSession;
import java.awt.Desktop;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import java.io.IOException;
import javafx.scene.control.ToggleGroup;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class CheckoutController {

    @FXML
    private Button btn_20;

    @FXML
    private Button btn_40;

    @FXML
    private Button btn_5;

    @FXML
    private Button btn_60;

    @FXML
    private Button btn_back;

    @FXML
    private Button btn_checkout;

    @FXML
    private Button btn_notNow;

    @FXML
    private Label txt_price;

    @FXML
    private Label lbl_deliveryAddress;

    @FXML
    private RadioButton rb_cash;

    @FXML
    private RadioButton rb_pandapay;

    private ToggleGroup paymentGroup;
    private Button previouslyClickedButton = null;
    private Float tipAmount = 0.0f;

    @FXML
    public void initialize() {
        paymentGroup = new ToggleGroup();
        rb_cash.setToggleGroup(paymentGroup);
        rb_pandapay.setToggleGroup(paymentGroup);
        setCheckoutDetails();
    }

    private void setCheckoutDetails() {
        // Set the restaurant name and delivery address from the session
        String restaurantName = CustomerDatabaseHandler.getRestaurantName(CustomerSession.getSelectedRestaurantID());
        
        String total = CustomerSession.getTotalPrice();
        txt_price.setText(total);
        
        String deliveryAddress = CustomerSession.getAddress() + ", Manila, Philippines";
        lbl_deliveryAddress.setText(deliveryAddress);
    }

    @FXML
    void handleCheckout(ActionEvent event) throws IOException {
        getDistance();
        computeDeliveryFee();
        computeTotalAmount();

        String customerID = CustomerSession.getCustomerID();
        String totalAmount = CustomerSession.getTotalPrice();
        String orderID = CustomerDatabaseHandler.generateOrderID();

        //boolean success = CustomerDatabaseHandler.checkoutCart(orderID, customerID, totalAmount);

        SwitchScene.switchScene(event, "/Customer/FXML/Receipt.fxml");

        // if (success) {
        //     System.out.println("Checkout successful!");
        //     SwitchScene.switchScene(event, "/Customer/FXML/Receipt.fxml");
        // } else {
        //     System.out.println("Checkout failed.");
        //     // optionally show alert
        // }
    }

    @FXML
    void paymentMethod(Desktop.ActionEvent event) {
        RadioButton selectedRadioButton = (RadioButton) event.getSource();
        String paymentMethod = selectedRadioButton.getText();
        System.out.println("Selected payment method: " + paymentMethod);
    }

    @FXML
    void tipYourRider(ActionEvent event) throws IOException {
        Button clickedButton = (Button) event.getSource();
        
        if (previouslyClickedButton != null) {
            previouslyClickedButton.setStyle("-fx-background-color: #f2f2f2; -fx-text-fill: #000000; -fx-background-radius: 25px; -fx-border-radius: 25px; -fx-border-color: #757575;");
        }

        clickedButton.setStyle("-fx-background-color: #757575; -fx-text-fill: white; -fx-background-radius: 25px; -fx-border-radius: 25px;");

        previouslyClickedButton = clickedButton;

        String buttonText = clickedButton.getText();

        if (clickedButton == btn_notNow) {
            System.out.println("No tip");
            tipAmount = 0.0f;
            CustomerSession.setTip("0.00");
        } else {
            tipAmount = Float.parseFloat(buttonText);
            String formattedTip = String.format("%.2f", tipAmount);
            tipAmount = Float.parseFloat(formattedTip);
            CustomerSession.setTip(formattedTip);
        }

    }

    private static String getDistance() {
        String restaurantID = CustomerSession.getSelectedRestaurantID();
        String customerID = CustomerSession.getCustomerID();

        String restaurantLocation = CustomerDatabaseHandler.getRestaurantLocation(restaurantID);
        restaurantLocation = restaurantLocation + ", Manila, Philippines";

        String address = CustomerSession.getAddress();
        address = address + ", Manila, Philippines";

        try {
            String API_KEY = "AIzaSyBuklDiWOw9gdQNHS1TrlZQ85x_ZVtu7A0";
            String origin = restaurantLocation;
            String destination = address;

            String urlStr = String.format(
                "https://maps.googleapis.com/maps/api/distancematrix/json?origins=%s&destinations=%s&key=%s",
                origin.replace(" ", "+"),
                destination.replace(" ", "+"),
                API_KEY
            );

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
            );
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // System.out.println("Raw JSON response: " + response.toString());

            // Parse the JSON response
            JSONObject json = new JSONObject(response.toString());
            JSONArray rows = json.getJSONArray("rows");
            JSONObject elements = rows.getJSONObject(0).getJSONArray("elements").getJSONObject(0);

            JSONObject jsonDistance = elements.getJSONObject("distance");
            JSONObject jsonDuration = elements.getJSONObject("duration");

            String distanceText = jsonDistance.getString("text");
            String durationText = jsonDuration.getString("text");

            float distance = Float.parseFloat(distanceText.replace(" km", "").trim());

            int duration = 0;

            if (durationText.contains("hour")) {
                // Extract hours
                String[] parts = durationText.split("hour");
                int hours = Integer.parseInt(parts[0].trim());
                duration += hours * 60;

                // Check if there are minutes after "hour"
                if (parts.length > 1 && parts[1].contains("min")) {
                    int minutes = Integer.parseInt(parts[1].replaceAll("[^\\d]", ""));
                    duration += minutes;
                }
            } else {
                // Only minutes (e.g. "45 mins")
                duration = Integer.parseInt(durationText.replaceAll("[^\\d]", ""));
            }

            CustomerSession.setDistance(distance);
            CustomerSession.setDuration(duration + 15);

            // System.out.println("Distance: " + distance);
            // System.out.println("Duration: " + duration);
            


        } catch (Exception e) {
            e.printStackTrace();
        }

        return "hello";
    }

    private static void computeDeliveryFee() {
        float distance = CustomerSession.getDistance();
        float deliveryFee;

        if (distance <= 1) {
            deliveryFee = 29;
        } else {
            deliveryFee = 29 + (distance - 1) * 5;
        }

        CustomerSession.setdeliveryFee(deliveryFee);
    }

    private static void computeTotalAmount() {
        float orderAmount = Float.parseFloat(CustomerSession.getOrderAmount());
        float tip = Float.parseFloat(CustomerSession.getTip());
        float  deliveryFee = CustomerSession.getDeliveryFee();

        Float totalAmount = deliveryFee + tip + orderAmount;

        String totalPrice = String.valueOf(totalAmount);

        CustomerSession.setTotalPrice(totalPrice);
    }

    @FXML
    void backToCart(ActionEvent event) throws IOException {
        SwitchScene.switchScene(event, "/Customer/FXML/Cart.fxml");
    }

}
