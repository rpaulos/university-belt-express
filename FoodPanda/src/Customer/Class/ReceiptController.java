package Customer.Class;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;

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

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ReceiptController {

    @FXML
    private Button btn_back;

    @FXML
    private Button btn_done;

    @FXML
    private GridPane cardGrid;

    @FXML
    private Label lbl_ETA;

    @FXML
    private Label lbl_deliveryFee;

    @FXML
    private Label lbl_orderAmount;

    @FXML
    private Label lbl_riderTip;

    @FXML
    private Label lbl_totalAmount;

    private String customerID = Customer.CustomerSession.getCustomerID();

    @FXML
    void initialize() {
        getDistance();
        computeDeliveryFee();
        computeTotalAmount();
        loadCartData();
        loadEstimatedTimeOfDelivery();
        setPaymentSummary();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void loadCartData() {
        String totalPrice = Customer.CustomerDatabaseHandler.getTotalPrice(customerID);
        // lbl_price.setText(totalPrice);

        // System.out.println("Total Price: " +totalPrice);

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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Customer/FXML/CheckoutCard.fxml"));
                AnchorPane card = loader.load();

                CheckoutCardController controller = loader.getController();
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

    private void setPaymentSummary() {
        String orderAmount = Customer.CustomerSession.getOrderAmount();
        String tip = Customer.CustomerSession.getTip();
        String totalAmount = Customer.CustomerSession.getTotalPrice();
        float deliveryFee = Customer.CustomerSession.getDeliveryFee();

        lbl_orderAmount.setText(orderAmount);
        lbl_riderTip.setText(tip);
        lbl_totalAmount.setText(totalAmount);
        lbl_deliveryFee.setText(String.format("%.2f", deliveryFee));
    }

    @FXML
    void backHome(ActionEvent event) throws IOException {
        CustomerDatabaseHandler.clearCart(customerID);
        SwitchScene.switchScene(event, "/Customer/FXML/Home.fxml");
    }

    @FXML
    void handleCheckout(ActionEvent event) throws IOException {
        getDistance();
        computeDeliveryFee();
        computeTotalAmount();

        String customerID = CustomerSession.getCustomerID();
        String totalAmount = CustomerSession.getTotalPrice();
        String orderID = CustomerDatabaseHandler.generateOrderID();

        String restaurantID = CustomerDatabaseHandler.getRestaurantIDinCart(customerID);

        CustomerDatabaseHandler.checkoutCart(orderID, customerID, restaurantID, totalAmount);

        showAlert(Alert.AlertType.INFORMATION, "Checkout Successful", "Your order will arive shortly");

        CustomerDatabaseHandler.clearCart(customerID);

        SwitchScene.switchScene(event, "/Customer/FXML/Home.fxml");
        // if (success) {
        //     System.out.println("Checkout successful!");
        //     SwitchScene.switchScene(event, "/Customer/FXML/Home.fxml");
        // } else {
        //     System.out.println("Checkout failed.");
        //     // optionally show alert
        // }
    }

    @FXML
    void loadEstimatedTimeOfDelivery() {
        lbl_ETA.setText(String.valueOf(CustomerSession.getDuration()));
    }

    private static String getDistance() {
        String restaurantID = CustomerSession.getSelectedRestaurantID();
        String customerID = CustomerSession.getCustomerID();

        String restaurantLocation = CustomerDatabaseHandler.getRestaurantLocation(restaurantID);
        restaurantLocation = restaurantLocation + ", Manila, Philippines";

        String address = CustomerSession.getAddress();
        address = address + ", Manila, Philippines";

        try {
            String API_KEY = "";
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

            System.out.println("Raw JSON response: " + response.toString());

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

            System.out.println("Distance: " + distance);
            System.out.println("Duration: " + duration);
            


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
    void backToCheckout(ActionEvent event) throws IOException {
        SwitchScene.switchScene(event, "/Customer/FXML/Checkout.fxml");
    }

}
