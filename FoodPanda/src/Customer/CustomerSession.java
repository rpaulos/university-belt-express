package Customer;

public class CustomerSession {
    private static String email;
    private static String customerID;
    private static String address;
    private static String selectedRestaurantID;
    private static String selectedProductID;
    private static String quantity;
    private static String totalPrice;
    private static String orderAmount;
    private static String tip;
    private static float distance;
    private static int duration;
    private static float deliveryFee;

    public static void setEmail(String email) {
        CustomerSession.email = email;
    }

    public static String getEmail() {
        return email;
    }

    public static void setCustomerID(String customerID) {
        CustomerSession.customerID = customerID;
    }

    public static String getCustomerID() {
        return customerID;
    }

    public static void setAddress(String address) {
        CustomerSession.address = address;
    }

    public static String getAddress() {
        return address;
    }

    public static void clearAddress() {
        address = null;
    }

    public static void setSelectedRestaurantID(String id) {
        CustomerSession.selectedRestaurantID = id;
    }

    public static String getSelectedRestaurantID() {
        return selectedRestaurantID;
    }

    public static void clearSelectedRestaurantID() {
        selectedRestaurantID = null;
    }

    public static void setSelectedProductID(String id) {
        CustomerSession.selectedProductID = id;
    }

    public static String getSelectedProductID() {
        return selectedProductID;
    }

    public static void clearSelectedProductID() {
        selectedProductID = null;
    }

    public static void setQuantity(String quantity) {
        CustomerSession.quantity = quantity;
    }

    public static String getQuantity() {
        return quantity;
    }

    public static void setTotalPrice(String totalPrice) {
        CustomerSession.totalPrice = totalPrice;
    }

    public static String getTotalPrice() {
        return totalPrice;
    }

    public static void setTip(String tip) {
        CustomerSession.tip = tip;
    }

    public static String getTip() {
        return tip;
    }  

    public static void clearTip() {
        tip = null;
    }

    public static void setOrderAmount(String orderAmount) {
        CustomerSession.orderAmount = orderAmount;
    }

    public static String getOrderAmount() {
        return orderAmount;
    }

    public static void setDistance(float distance) {
        CustomerSession.distance = distance;
    }

    public static float getDistance() {
        return distance;
    }  

    public static void setdeliveryFee(float deliveryFee) {
        CustomerSession.deliveryFee = deliveryFee;
    }

    public static float getDeliveryFee() {
        return deliveryFee;
    }  

    public static void setDuration(int duration) {
        CustomerSession.duration = duration;
    }

    public static int getDuration() {
        return duration;
    }  

    public static void clearSession() {
        email = null;
        selectedRestaurantID = null;
        selectedProductID = null;
        customerID = null;
        address = null;
        selectedProductID = null;
        quantity = null;
        tip = null;
        totalPrice = null;
        orderAmount = null;
        distance = 0.02f;
        duration = 0;

    }
}
