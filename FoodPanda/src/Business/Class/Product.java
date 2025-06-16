package Business.Class;

public class Product {
    private String id;
    private String restaurantId;
    private String name;
    private String description;
    private int quantity;
    private double price;
    private String imagePath;

    public Product(String id, String restaurantId, String name, String description, int quantity, double price, String imagePath) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.imagePath = imagePath;
    }

    // Getters (no setters if immutable)
    public String getId() { 
        return id; 
    }

    public String getRestaurantId() { 
        return restaurantId; 
    }

    public String getProductName() { 
        return name; 
    }

    public String getProductDescription() { 
        return description; 
    }

    public int getProductQuantity() { 
        return quantity; 
    }

    public double getProductPrice() { 
        return price; 
    }
    public String getProductImagePath() { 
        return imagePath; 
    }
}
