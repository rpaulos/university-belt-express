package Customer;

import Database.DatabaseCredentials;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import Admin.Class.OrderRow;

import com.mysql.cj.x.protobuf.MysqlxPrepare.Prepare;

import Customer.Class.CartItem;
import Customer.Class.ProductCardController;
import Customer.Class.ProductItem;
import Customer.Class.RestaurantItem;
import Customer.Class.RestaurantsItem;

public class CustomerDatabaseHandler {
    private static CustomerDatabaseHandler handler = null;
    private static Statement stmt = null;
    private static PreparedStatement pstatement = null;

    public static String dburl = DatabaseCredentials.ignoreDburl;
    public static String userName = DatabaseCredentials.ignoreUserName;
    public static String password = DatabaseCredentials.ignorePassword;

    public static CustomerDatabaseHandler getInstance() {
        if (handler == null) {
            handler = new CustomerDatabaseHandler();
        }
        return handler;
    }

    public static Connection getDBConnection()
    {
        Connection connection = null;

        try
        {
            connection = DriverManager.getConnection(dburl, userName, password);

        } catch (Exception e){
            e.printStackTrace();
        }

        return connection;
    }

    public ResultSet execQuery(String query) {
        ResultSet result;
        try {
            stmt = getDBConnection().createStatement();
            result = stmt.executeQuery(query);
        }
        catch (SQLException ex) {
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return null;
        }
        finally {
        }
        return result;
    }

    public int execUpdateQuery(String query) {
        int affectedRows = 0;
        try {
            stmt = getDBConnection().createStatement();
            affectedRows = stmt.executeUpdate(query);
        } catch (SQLException ex) {
            System.out.println("Not working");
        }
        return affectedRows;
    }

    // Login validation of email and password of the customer
    public static boolean validateLoginCredentials(String email, String password) {
        getInstance();

        String query = "SELECT * FROM customer WHERE customer_email = ? AND customer_password = ?";
        
        try (Connection conn = getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);

            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error validating credentials: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Checks if email exists in the database
    public static boolean emailExists(String email) {
        getInstance();

        String query = "SELECT * FROM customer WHERE customer_email = ?";
        
        try (Connection conn = getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);

            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error validating credentials: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Checks if customerID exists
    public static boolean validateCustomerID(String customerID) {
        getInstance();

        String query = "SELECT * FROM customer WHERE customer_ID = ?";
        
        try (Connection conn = getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, customerID);

            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error validating credentials: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Checks if restaurantID exists
    public static boolean validateRestaurantID(String restaurantID) {
        getInstance();

        String query = "SELECT * FROM restaurant WHERE restaurant_ID = ?";
        
        try (Connection conn = getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, restaurantID);

            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error validating credentials: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Checks if phone number exists in the database
    public static boolean phoneNumberExists(String phoneNumber) {
        getInstance();

        String query = "SELECT * FROM customer WHERE customer_phone_number = ?";
        
        try (Connection conn = getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, phoneNumber);

            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error validating credentials: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Customer ID generator
    public static String generateCustomerID() {
        getInstance();

        String currentYear = String.valueOf(Year.now().getValue());
        String prefix = currentYear + "-";
        String query = "SELECT customer_id FROM customer WHERE customer_id LIKE ? ORDER BY customer_id DESC LIMIT 1";

        try(Connection conn = getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, prefix + "%");
            ResultSet result = pstmt.executeQuery();

            int nextNumber = 1;

            if (result.next()) {
                String lastID = result.getString("customer_id");
                String numberPart = lastID.split("-")[1];
                nextNumber = Integer.parseInt(numberPart) + 1;
            }

            return String.format("%s%05d", prefix, nextNumber);

        } catch (SQLException e) {
            System.out.println("Error getting customer ID: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    // Customer Location ID generator
    public static String generateCustomerLocationID(String address, String city, String zip) {
        getInstance();

        String cityCode = city.trim().toUpperCase().replaceAll("\\s+", "").substring(0, 3);
        String prefix = cityCode + "_";

        String query = "SELECT customer_location_ID FROM customer_location WHERE customer_location_ID LIKE ? ORDER BY customer_location_ID DESC LIMIT 1";

        try(Connection conn = getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, prefix + "%");
            ResultSet result = pstmt.executeQuery();

            int nextNumber = 1;

            if (result.next()) {
                String lastID = result.getString("customer_location_ID");
                String[] parts = lastID.split("_");
                if (parts.length == 2) {
                    nextNumber = Integer.parseInt(parts[1]) + 1;
                }
            }

            return String.format("%s%05d", prefix, nextNumber);

        } catch (SQLException e) {
            System.out.println("Error getting customer ID: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    // Insert customer location ID
    public static boolean insertCustomerLocation(String customerLocationID, String city, String address, String ZIP) {
        getInstance();

        String query = "INSERT INTO customer_location (customer_location_ID, city, address, zip_code) VALUES (?, ?, ?, ?)";

        try(Connection conn = getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, customerLocationID);
            pstmt.setString(2, city);
            pstmt.setString(3, address);
            pstmt.setString(4, ZIP);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error inserting customer location: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Generate the customer ID based on the school the customer is enrolled
    public static String getUniversityID(String selectedSchool) {
        return switch (selectedSchool) {
            case "University of Santo Tomas" -> "UST-0001";
            case "National University - Manila" -> "NU-0002";
            case "Far Eastern University" -> "FEU-0003";
            case "Centro Escolar University" -> "CEU-0004";
            default -> "UNKNOWN";
        };
    }

    // Create the account
    public static boolean insertCustomer(String email, String password, String firstName, String lastName, String phoneNumber, String selectedCity) {
        getInstance();

        String customerID = generateCustomerID();
        //String universityID = getUniversityID(selectedSchool);

        String query = "INSERT INTO customer (customer_id, customer_email, customer_password, customer_first_name, customer_last_name, customer_phone_number, customer_location_ID) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, customerID);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.setString(4, firstName);
            pstmt.setString(5, lastName);
            pstmt.setString(6, phoneNumber);
            pstmt.setString(7, selectedCity);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Error inserting customer: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static String getbusinessOwnerID(String restaurantID) {
        getInstance();

        String query = "SELECT business_owner_ID FROM business_owner WHERE restaurant_ID = ?";
        
        try (Connection conn = getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, restaurantID);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                return result.getString("business_owner_ID");
            }

        } catch (SQLException e) {
            System.out.println("Error getting business owner ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static String getBusinessOwnerEmail(String businessOwnerID) {
        getInstance();

        String query = "SELECT owner_email FROM business_owner WHERE business_owner_ID = ?";
        
        try (Connection conn = getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, businessOwnerID);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                return result.getString("owner_email");
            }

        } catch (SQLException e) {
            System.out.println("Error getting business owner email: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static String getBusinessName(String restaurantID) {
        getInstance();

        String query = "SELECT restaurant_name FROM restaurant WHERE restaurant_ID = ?";
        
        try (Connection conn = getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, restaurantID);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                return result.getString("restaurant_name");
            }

        } catch (SQLException e) {
            System.out.println("Error getting restaurant name: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static boolean deleteRestaurantByID(String restaurantID) {
        String query = "DELETE FROM restaurant WHERE restaurant_ID = ?";
        try (Connection conn = getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, restaurantID);
            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;

        }
    }

    public static boolean deleteCustomerByID(String customerID) {
        String query = "DELETE FROM customer WHERE customer_ID = ?";
        try (Connection conn = getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, customerID);
            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
            
        }
    }

    public static String getFirstName(String email) {
        getInstance();

        String query = "SELECT customer_first_name FROM customer WHERE customer_email = ?";
        
        try (Connection conn = getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                return result.getString("customer_first_name");
            }

        } catch (SQLException e) {
            System.out.println("Error getting first name: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    public static String getFirstNamebyID(String customerID) {
        getInstance();

        String query = "SELECT customer_first_name FROM customer WHERE customer_ID = ?";
        
        try (Connection conn = getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, customerID);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                return result.getString("customer_first_name");
            }

        } catch (SQLException e) {
            System.out.println("Error getting first name: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static String getBusinessOwnerFirstNameByID(String restaurantID) {
        getInstance();

        String query = "SELECT owner_first_name FROM business_owner WHERE restaurant_ID = ?";
        
        try (Connection conn = getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, restaurantID);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                return result.getString("owner_first_name");
            }

        } catch (SQLException e) {
            System.out.println("Error getting first name: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static String getLastName(String email) {
        getInstance();

        String query = "SELECT customer_last_name FROM customer WHERE customer_email = ?";
        
        try (Connection conn = getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                return result.getString("customer_last_name");
            }

        } catch (SQLException e) {
            System.out.println("Error getting last name: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static String getLastNamebyID(String customerID) {
        getInstance();

        String query = "SELECT customer_last_name FROM customer WHERE customer_ID = ?";
        
        try (Connection conn = getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, customerID);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                return result.getString("customer_last_name");
            }

        } catch (SQLException e) {
            System.out.println("Error getting first name: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static String getBusinessOwnerLastNameByID(String restaurantID) {
        getInstance();

        String query = "SELECT owner_last_name FROM business_owner WHERE restaurant_ID = ?";
        
        try (Connection conn = getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, restaurantID);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                return result.getString("owner_last_name");
            }

        } catch (SQLException e) {
            System.out.println("Error getting last name: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static String getCustomerEmail(String customerID) {
        getInstance();

        String query = "SELECT customer_email FROM customer WHERE customer_ID = ?";
        
        try (Connection conn = getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, customerID);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                return result.getString("customer_email");
            }

        } catch (SQLException e) {
            System.out.println("Error getting  email: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static String getCustomerPhoneNumber(String customerID) {
        getInstance();

        String query = "SELECT customer_phone_number FROM customer WHERE customer_ID = ?";
        
        try (Connection conn = getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, customerID);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                return result.getString("customer_phone_number");
            }

        } catch (SQLException e) {
            System.out.println("Error getting phone number: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static String getCustomerTotalSpent(String customerID) {
        getInstance();

        String query = "SELECT SUM(amount) AS total_amount FROM orders WHERE customer_ID = ?";
        
        try (Connection conn = getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, customerID);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                return result.getString("total_amount");
            }

        } catch (SQLException e) {
            System.out.println("Error getting amount: " + e.getMessage());
            e.printStackTrace();
        }
        return "0.00";
    }

    public static String getRestaurantEarnings(String restaurantID) {
        getInstance();

        String query = "SELECT SUM(amount) AS total_amount FROM orders WHERE restaurant_ID = ?";
        
        try (Connection conn = getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, restaurantID);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                return result.getString("total_amount");
            }

        } catch (SQLException e) {
            System.out.println("Error getting amount: " + e.getMessage());
            e.printStackTrace();
        }
        return "0.00";
    }

    public static List<RestaurantItem> getRestaurantItems() {
        List<RestaurantItem> restaurantItems = new ArrayList<>();

        String query = "SELECT r.restaurant_ID, r.restaurant_name, r.restaurant_header_path, l.address, r.price_range " +
                       "FROM Restaurant r " +
                       "JOIN restaurant_location l ON r.restaurant_location_ID = l.restaurant_location_ID";

        try (Connection conn = DriverManager.getConnection(dburl, userName, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String id = rs.getString("restaurant_ID");
                String name = rs.getString("restaurant_name");
                String headerPath = rs.getString("restaurant_header_path");
                String address = rs.getString("address");
                String priceRange = rs.getString("price_range");
                restaurantItems.add(new RestaurantItem(name, address, headerPath, id, priceRange));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return restaurantItems;
    }

    public static List<RestaurantItem> getFilteredRestaurants(String priceRange) {
        List<RestaurantItem> restaurantItems = new ArrayList<>();

        String query = "SELECT r.restaurant_ID, r.restaurant_name, r.restaurant_header_path, l.address, r.price_range " +
               "FROM Restaurant r " +
               "JOIN restaurant_location l ON r.restaurant_location_ID = l.restaurant_location_ID " + 
               "WHERE r.price_range = ?";

        try (Connection conn = DriverManager.getConnection(dburl, userName, password);
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, priceRange); 

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String id = rs.getString("restaurant_ID");
                    String name = rs.getString("restaurant_name");
                    String headerPath = rs.getString("restaurant_header_path");
                    String address = rs.getString("address");
                    String range = rs.getString("price_range"); 
                    restaurantItems.add(new RestaurantItem(name, address, headerPath, id, range));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return restaurantItems;
    }

    public static List<ProductItem> getProductItems(String restaurant_ID) {
        List<ProductItem> productItems = new ArrayList<>();

        String query = "SELECT * FROM product WHERE restaurant_ID = ? AND product_quantity >= 1";

        try (Connection conn = DriverManager.getConnection(dburl, userName, password);
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, restaurant_ID);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String id = rs.getString("product_ID");
                    String name = rs.getString("product_name");
                    String price = String.format("%.2f", rs.getDouble("product_price"));
                    String description = rs.getString("product_desc");
                    String imagePath = rs.getString("product_image_path");
                    productItems.add(new ProductItem(id, name, price, description, imagePath));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productItems;
    }

    public static List<ProductItem> getAllProductsInRestaurant(String restaurant_ID) {
        List<ProductItem> productItems = new ArrayList<>();

        String query = "SELECT * FROM product WHERE restaurant_ID = ?";

        try (Connection conn = DriverManager.getConnection(dburl, userName, password);
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, restaurant_ID);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String id = rs.getString("product_ID");
                    String name = rs.getString("product_name");
                    String price = String.format("%.2f", rs.getDouble("product_price"));
                    String description = rs.getString("product_desc");
                    String imagePath = rs.getString("product_image_path");
                    productItems.add(new ProductItem(id, name, price, description, imagePath));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productItems;
    }

    public static List<ProductItem> getAllProducts() {
        List<ProductItem> productItems = new ArrayList<>();

        String query = "SELECT * FROM product";

        try (Connection conn = DriverManager.getConnection(dburl, userName, password);
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String id = rs.getString("product_ID");
                    String name = rs.getString("product_name");
                    String price = String.format("%.2f", rs.getDouble("product_price"));
                    String description = rs.getString("product_desc");
                    String imagePath = rs.getString("product_image_path");
                    productItems.add(new ProductItem(id, name, price, description, imagePath));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productItems;
    }
    

    public static String getCustomerLocationID(String email) {
        getInstance();

        String query = "SELECT customer_location_ID FROM customer WHERE customer_email = ?";

        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                return result.getString("customer_location_ID");
            }

        } catch (SQLException e) {
            System.out.println("Error getting customer location ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    

    public static String getAddress(String customerLocationID) {
        getInstance();

        String query = "SELECT address FROM customer_location WHERE customer_location_ID = ?";

        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, customerLocationID);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                return result.getString("address");
            }

        } catch (SQLException e) {
            System.out.println("Error getting address: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static String getRestaurantName(String restaurantID) {
        getInstance();

        String query = "SELECT restaurant_name FROM Restaurant WHERE restaurant_ID = ?";

        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, restaurantID);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                return result.getString("restaurant_name");
            }

        } catch (SQLException e) {
            System.out.println("Error getting restaurant name: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static String getProductName(String productID) {
        getInstance();

        String query = "SELECT product_name FROM Product WHERE product_ID = ?";

        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, productID);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                return result.getString("product_name");
            }

        } catch (SQLException e) {
            System.out.println("Error getting product name: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    public static String getProductPrice(String productID) {
        getInstance();

        String query = "SELECT product_price FROM Product WHERE product_ID = ?";

        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, productID);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                return String.format("%.2f", result.getDouble("product_price"));
            }

        } catch (SQLException e) {
            System.out.println("Error getting product price: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static String getProductDescription(String productID) {
        getInstance();

        String query = "SELECT product_desc FROM Product WHERE product_ID = ?";

        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, productID);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                return result.getString("product_desc");
            }

        } catch (SQLException e) {
            System.out.println("Error getting product description: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static String getProductQuantity(String productID) {
        getInstance();

        String query = "SELECT product_quantity FROM Product WHERE product_ID = ?";

        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, productID);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                return String.valueOf(result.getInt("product_quantity"));
            }

        } catch (SQLException e) {
            System.out.println("Error getting product quantity: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static String getCustomerID(String email) {
        getInstance();

        String query = "SELECT customer_id FROM customer WHERE customer_email = ?";

        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                return result.getString("customer_id");
            }

        } catch (SQLException e) {
            System.out.println("Error getting customer ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static void addToCart(String customerID, String productID, String restaurantID, String quantity) {
        getInstance();

        String query = "INSERT INTO cart (customer_ID, product_ID, restaurant_ID, quantity) VALUES (?, ?, ?, ?)";

        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, customerID);
            pstmt.setString(2, productID);
            pstmt.setString(3, restaurantID);
            pstmt.setString(4, quantity);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Product added to cart successfully.");
            } else {
                System.out.println("Failed to add product to cart.");
            }

        } catch (SQLException e) {
            System.out.println("Error adding to cart: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void addCartQuantity(String customerID, String productID, String restaurantID, String quantity) {
        getInstance();

        String query = "UPDATE cart SET quantity = quantity + ? WHERE customer_ID = ? AND product_ID = ? AND restaurant_ID = ?";

        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, quantity);
            pstmt.setString(2, customerID);
            pstmt.setString(3, productID);
            pstmt.setString(4, restaurantID);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Cart quantity updated successfully.");
            } else {
                System.out.println("Failed to update cart quantity.");
            }

        } catch (SQLException e) {
            System.out.println("Error updating cart quantity: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean isProductInCart(String customerID, String productID, String restaurantID) {
        getInstance();

        String query = "SELECT * FROM cart WHERE customer_ID = ? AND product_ID = ? AND restaurant_ID = ?";

        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, customerID);
            pstmt.setString(2, productID);
            pstmt.setString(3, restaurantID);

            ResultSet result = pstmt.executeQuery();

            return result.next();

        } catch (SQLException e) {
            System.out.println("Error checking if product is in cart: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public static int getMaxQuantityInCart(String customerID, String productID, String restaurantID) {
        getInstance();

        String query = "SELECT product_quantity FROM Product WHERE product_ID = ?";

        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, productID);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                return result.getInt("product_quantity");
            }

        } catch (SQLException e) {
            System.out.println("Error getting max quantity in cart: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public static int getCartQuantity(String customerID, String productID, String restaurantID) {
        getInstance();

        String query = "SELECT quantity FROM cart WHERE customer_ID = ? AND product_ID = ? AND restaurant_ID = ?";

        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, customerID);
            pstmt.setString(2, productID);
            pstmt.setString(3, restaurantID);

            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                return result.getInt("quantity");
            }

        } catch (SQLException e) {
            System.out.println("Error getting quantity in cart: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public static String getRestaurantIDinCart(String customerID) {
        getInstance();

        String query = "SELECT restaurant_ID FROM cart WHERE customer_ID = ?";

        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, customerID);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                return result.getString("restaurant_ID");
            }

        } catch (SQLException e) {
            System.out.println("Error getting restaurant ID in cart: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static String getTotalPrice(String customerID) {
        getInstance();

        String query = "SELECT SUM(p.product_price * c.quantity) AS total_price " +
                       "FROM cart c " +
                       "JOIN product p ON c.product_ID = p.product_ID " +
                       "WHERE c.customer_ID = ?";

        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, customerID);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                return String.format("%.2f", result.getDouble("total_price"));
            }

        } catch (SQLException e) {
            System.out.println("Error getting total price: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static List<CartItem> getCartItems(String customerID) {
        List<CartItem> cartItems = new ArrayList<>();

        String query = "SELECT c.customer_ID, c.product_ID, p.product_name, p.product_price, p.product_desc, c.quantity " +
                       "FROM cart c " +
                       "JOIN product p ON c.product_ID = p.product_ID " +
                       "WHERE c.customer_ID = ?";

        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, customerID);
            ResultSet result = pstmt.executeQuery();

            while (result.next()) {
                //String customerID = result.getString("customer_ID");
                String productID = result.getString("product_ID");
                String productName = result.getString("product_name");
                String productPrice = result.getString("product_price");
                String productDesc = result.getString("product_desc");
                String productQuantity = result.getString("quantity");

                CartItem item = new CartItem(customerID, productID, productName, productPrice, productDesc, productQuantity);
                cartItems.add(item);
            }

        } catch (SQLException e) {
            System.out.println("Error getting cart items: " + e.getMessage());
            e.printStackTrace();
        }
        return cartItems;
    }

    public static String getProductStock(String productID) {
        getInstance();

        String query = "SELECT product_quantity FROM product WHERE product_ID = ?";

        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, productID);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                return result.getString("product_quantity");
            }

        } catch (SQLException e) {
            System.out.println("Error getting product stock: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static String removeCartItem(String customerID, String productID) {
        getInstance();

        String query = "DELETE FROM cart WHERE customer_ID = ? AND product_ID = ?";

        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, customerID);
            pstmt.setString(2, productID);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                return "Item removed from cart successfully.";
            } else {
                return "Failed to remove item from cart.";
            }

        } catch (SQLException e) {
            System.out.println("Error removing cart item: " + e.getMessage());
            e.printStackTrace();
        }
        return "Error occurred while removing item from cart.";
    }

    public static String updateCartItemQuantity(String customerID, String productID, int quantity) {
        getInstance();

        String query = "UPDATE cart SET quantity = ? WHERE customer_ID = ? AND product_ID = ?";

        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, quantity);
            pstmt.setString(2, customerID);
            pstmt.setString(3, productID);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                return "Quantity updated successfully.";
            } else {
                return "Failed to update item quantity.";
            }

        } catch (SQLException e) {
            System.out.println("Error updating cart item: " + e.getMessage());
            e.printStackTrace();
        }
        return "Error occurred while updating item from cart.";
    }

    public static String generateOrderID() {
        getInstance();

        String currentYear = String.valueOf(Year.now().getValue());
        String prefix = currentYear + "-";
        String query = "SELECT order_ID FROM orders WHERE order_ID LIKE ? ORDER BY order_ID DESC LIMIT 1";

        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, prefix + "%");
            ResultSet result = pstmt.executeQuery();

            int nextNumber = 1;

            if (result.next()) {
                String lastID = result.getString("order_ID");
                String numberPart = lastID.split("-")[1];
                nextNumber = Integer.parseInt(numberPart) + 1;
            }

            return String.format("%s%05d", prefix, nextNumber);

        } catch (SQLException e) {
            System.out.println("Error getting order ID: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
        
    }

    public static void clearCart(String customerID) {
        getInstance();

        String query = "DELETE FROM cart WHERE customer_ID = ?";

        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, customerID);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Cart cleared successfully.");
            } else {
                System.out.println("Failed to clear cart.");
            }

        } catch (SQLException e) {
            System.out.println("Error clearing cart: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void insertOrder(String orderID, String customerID, String restaurantID, float totalAmount) {
        getInstance();

        String query = "INSERT INTO orders (order_ID, customer_ID, restaurant_ID, amount) VALUES (?, ?, ?, ?)";

        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, orderID);
            pstmt.setString(2, customerID);
            pstmt.setString(3, restaurantID);
            pstmt.setFloat(4, totalAmount);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Order inserted successfully.");
            } else {
                System.out.println("Failed to insert order.");
            }

        } catch (SQLException e) {
            System.out.println("Error inserting order: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean checkoutCart(String orderID, String customerID, String restaurantID, String totalAmount) {
        Connection conn = getDBConnection();
        PreparedStatement updateStockStmt = null;
        PreparedStatement getCartItemsStmt = null;
        PreparedStatement insertOrderStmt = null;
        PreparedStatement deleteCartStmt = null;

        try {
            conn.setAutoCommit(false); // Start transaction

            // 1. Get cart items
            String getCartItemsQuery = "SELECT * FROM cart WHERE customer_ID = ?";
            getCartItemsStmt = conn.prepareStatement(getCartItemsQuery);
            getCartItemsStmt.setString(1, customerID);
            ResultSet cartItems = getCartItemsStmt.executeQuery();

            //String restaurantID = null;

            // 2. Update product stock
            while (cartItems.next()) {
                String productID = cartItems.getString("product_ID");
                int quantity = cartItems.getInt("quantity");
                restaurantID = cartItems.getString("restaurant_ID"); // assume all from one resto

                // Deduct stock
                String updateStockQuery = "UPDATE product SET product_quantity = product_quantity - ? WHERE product_ID = ?";
                updateStockStmt = conn.prepareStatement(updateStockQuery);
                updateStockStmt.setInt(1, quantity);
                updateStockStmt.setString(2, productID);
                updateStockStmt.executeUpdate();
            }

            // 3. Insert into orders
            //String orderID = UUID.randomUUID().toString();
            String insertOrderQuery = "INSERT INTO orders (order_ID, customer_ID, restaurant_ID, amount) VALUES (?, ?, ?, ?)";
            insertOrderStmt = conn.prepareStatement(insertOrderQuery);
            insertOrderStmt.setString(1, orderID);
            insertOrderStmt.setString(2, customerID);
            insertOrderStmt.setString(3, restaurantID);
            insertOrderStmt.setBigDecimal(4, new BigDecimal(totalAmount));
            insertOrderStmt.executeUpdate();

            // 4. Clear the cart
            // String deleteCartQuery = "DELETE FROM cart WHERE customer_ID = ?";
            // deleteCartStmt = conn.prepareStatement(deleteCartQuery);
            // deleteCartStmt.setString(1, customerID);
            // deleteCartStmt.executeUpdate();

            conn.commit();
            return true;

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
            return false;

        } finally {
            try {
                if (updateStockStmt != null) updateStockStmt.close();
                if (getCartItemsStmt != null) getCartItemsStmt.close();
                if (insertOrderStmt != null) insertOrderStmt.close();
                if (deleteCartStmt != null) deleteCartStmt.close();
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getRestaurantLocation(String restaurantID) {
        getInstance();

        String query = "SELECT l.address FROM Restaurant r " +
                       "JOIN restaurant_location l ON r.restaurant_location_ID = l.restaurant_location_ID " +
                       "WHERE r.restaurant_ID = ?";

        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, restaurantID);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                return result.getString("address");
            }

        } catch (SQLException e) {
            System.out.println("Error getting restaurant location: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static boolean deleteProductByID(String productID, String restaurantID) {
        String query = "DELETE FROM product WHERE product_name = ? AND restaurant_ID = ?";
        
        try (Connection conn = getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, productID);
            pstmt.setString(2, restaurantID);
            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        
    }


    // public static List<RestaurantItem> getFilteredRestaurants(String priceRange) {
    //     List<RestaurantItem> restaurantItems = new ArrayList<>();

    //     String query = "SELECT r.restaurant_ID, r.restaurant_name, r.restaurant_header_path, l.address, r.price_range " +
    //            "FROM Restaurant r " +
    //            "JOIN restaurant_location l ON r.restaurant_location_ID = l.restaurant_location_ID " + 
    //            "WHERE r.price_range = ?";

    //     try (Connection conn = DriverManager.getConnection(dburl, userName, password);
    //         PreparedStatement pstmt = conn.prepareStatement(query)) {

    //         pstmt.setString(1, priceRange); 

    //         try (ResultSet rs = pstmt.executeQuery()) {
    //             while (rs.next()) {
    //                 String id = rs.getString("restaurant_ID");
    //                 String name = rs.getString("restaurant_name");
    //                 String headerPath = rs.getString("restaurant_header_path");
    //                 String address = rs.getString("address");
    //                 String range = rs.getString("price_range"); 
    //                 restaurantItems.add(new RestaurantItem(name, address, headerPath, id, range));
    //             }
    //         }

    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    //     return restaurantItems;
    // }

    public static List<OrderRow> getOrdersByCustomerID(String customerID) {
        List<OrderRow> orders = new ArrayList<>();
        String query = "SELECT order_ID, customer_ID, restaurant_ID, amount FROM orders WHERE customer_ID = ?";

        try (Connection conn = getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, customerID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                OrderRow order = new OrderRow(
                    rs.getString("order_ID"),
                    rs.getString("customer_ID"),
                    rs.getString("restaurant_ID"),
                    rs.getDouble("amount")
                );
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    public static List<OrderRow> getAllCustomerOrders() {
        List<OrderRow> orders = new ArrayList<>();
        String query = "SELECT order_ID, customer_ID, restaurant_ID, amount FROM orders";

        try (Connection conn = getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                OrderRow order = new OrderRow(
                    rs.getString("order_ID"),
                    rs.getString("customer_ID"),
                    rs.getString("restaurant_ID"),
                    rs.getDouble("amount")
                );
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    public static List<OrderRow> getAllOwnerOrders() {
        List<OrderRow> orders = new ArrayList<>();
        String query = "SELECT order_ID, customer_ID, restaurant_ID, amount FROM orders";

        try (Connection conn = getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                OrderRow order = new OrderRow(
                    rs.getString("order_ID"),
                    rs.getString("customer_ID"),
                    rs.getString("restaurant_ID"),
                    rs.getDouble("amount")
                );
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    public static List<OrderRow> getOrdersByRestaurantID(String restaurantID) {
        List<OrderRow> orders = new ArrayList<>();
        String query = "SELECT order_ID, customer_ID, restaurant_ID, amount FROM orders WHERE restaurant_ID = ?";

        try (Connection conn = getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, restaurantID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                OrderRow order = new OrderRow(
                    rs.getString("order_ID"),
                    rs.getString("customer_ID"),
                    rs.getString("restaurant_ID"),
                    rs.getDouble("amount")
                );
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }
}
