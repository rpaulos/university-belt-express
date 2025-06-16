package Business;

public class BusinessSession {
    private static String email;
    private static String restaurantID;
    private static String business_owner_ID;

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        BusinessSession.email = email;
    }

    public static String getRestaurantID() {
        return restaurantID;
    }

    public static void setRestaurantID(String restaurantID) {
        BusinessSession.restaurantID = restaurantID;
    }

    public static String getBusinessOwnerID() {
        return business_owner_ID;
    }

    public static void setBusinessOwnerID(String business_owner_ID) {
        BusinessSession.business_owner_ID = business_owner_ID;
    }

    public static void clearSession() {
        email = null;
        restaurantID = null;
        business_owner_ID = null;
    }
}