package Customer.Class;

public class RestaurantItem {
    private String restaurant_name;
    private String restaurant_address;
    private String restaurant_header_path;
    private String restaurantID;
    private String price_range;

    public RestaurantItem(String restaurant_name, String restaurant_address, String restaurant_header_path, String restaurantID, String price_range) {
        this.restaurant_name = restaurant_name;
        this.restaurant_address = restaurant_address;
        this.restaurant_header_path = restaurant_header_path;
        this.restaurantID = restaurantID;
        this.price_range = price_range;
    }

    public String getName() {
        return restaurant_name;
    }

    public String getAddress() {
        return restaurant_address;
    }

    public String getHeaderPath() {
        return restaurant_header_path;
    }

    public String getRestaurantID() {
        return restaurantID;
    }

    public String getPriceRange() {
        return price_range;
    }

}
