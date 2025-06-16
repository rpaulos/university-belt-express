package Customer.Class;

public class ProductItem {
    private String product_id;
    private String product_name;
    private String product_price;
    private String product_description;
    private String product_imagePath;

    public ProductItem(String id, String name, String price, String description, String imagePath) {
        this.product_id = id;
        this.product_name = name;
        this.product_price = price;
        this.product_description = description;
        this.product_imagePath = imagePath;
    }

    public String getProductID() {
        return product_id;
    }

    public String getProductName() {
        return product_name;
    }

    public String getProductPrice() {
        return product_price;
    }

    public String getProductDescription() {
        return product_description;
    }

    public String getProductImagePath() {
        return product_imagePath;
    }
}
