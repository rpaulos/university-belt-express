package Customer.Class;

public class CartItem {
    private String customerID;
    private String productID;
    private String productName;
    private String productPrice;
    private String productDesc;
    private String productQuantity;

    public CartItem(String customerID, String productID, String productName, String productPrice, String productDesc, String productQuantity) {
        this.customerID = customerID;
        this.productID = productID;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productDesc = productDesc;
        this.productQuantity = productQuantity;
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getProductID() {
        return productID;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public String getProductQuantity() {
        return productQuantity;
    }
}
