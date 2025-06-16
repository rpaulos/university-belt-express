package Admin.Class;

public class OrderRow {
    private String orderID;
    private String customerID;
    private String restaurantID;
    private double amount;

    public OrderRow(String orderID, String customerID, String restaurantID, double amount) {
        this.orderID = orderID;
        this.customerID = customerID;
        this.restaurantID = restaurantID;
        this.amount = amount;
    }

    public String getOrderID() { 
        return orderID; 
    
    }
    public String getCustomerID() { 
        return customerID; 
    
    }
    public String getRestaurantID() { 
        return restaurantID; 
    }

    public double getAmount() { 
        return amount; 
    }
}