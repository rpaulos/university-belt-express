package Customer.Class;

public class FoodItem {
    private String name;
    private String price;
    private String restaurant;

    public FoodItem(String name, String price, String restaurant) {
        this.name = name;
        this.price = price;
        this.restaurant = restaurant;
    }

    public String getName() { return name; }
    public String getPrice() { return price; }
    public String getRestaurant() { return restaurant; }
}
