package Business;

import Database.DatabaseCredentials;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Business.Class.Product;

public class BusinessDatabaseHandler {
    private static BusinessDatabaseHandler handler = null;
    private static Statement stmt = null;
    private static PreparedStatement pstatement = null;

    public static String dburl = DatabaseCredentials.ignoreDburl;
    public static String userName = DatabaseCredentials.ignoreUserName;
    public static String password = DatabaseCredentials.ignorePassword;

    public static BusinessDatabaseHandler getInstance() {
        if (handler == null) {
            handler = new BusinessDatabaseHandler();
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

        // Login validation of email and password of the business owner
    public static boolean validateBusinessOwnerLogin(String oemail, String opassword) {
        getInstance();

        String query = "SELECT * FROM business_owner WHERE owner_email = ? AND owner_password = ?";
    
        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, oemail);
            pstmt.setString(2, opassword);

            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
             return true;
            }

        } catch (SQLException e) {
            System.out.println("Error validating business owner credentials: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Checks if a business owner email exists in the database
    public static boolean emailExists(String email) {
        getInstance();

        String query = "SELECT * FROM business_owner WHERE owner_email = ?";
    
        try (Connection conn = getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);

            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
             return true;
            }

        } catch (SQLException e) {
            System.out.println("Error checking business owner email: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Checks if company name exists in the database
    public static boolean companyNameExists(String companyName) {
        getInstance();

        String query = "SELECT * FROM restaurant WHERE restaurant_name = ?";

        try (Connection conn = getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, companyName);

            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
             return true;
            }

        } catch (SQLException e) {
            System.out.println("Error checking business owner company name: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Restaurant Location ID generator
    public static String generateRestaurantLocationID(String address, String city, String zip) {
        getInstance();

        String cityCode = city.trim().toUpperCase().replaceAll("\\s+", "").substring(0, 3);
        String prefix = "RL" + cityCode + "_";

        String query = "SELECT restaurant_location_ID FROM restaurant_location WHERE restaurant_location_ID LIKE ? ORDER BY restaurant_location_ID DESC LIMIT 1";

        try(Connection conn = getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, prefix + "%");
            ResultSet result = pstmt.executeQuery();

            int nextNumber = 1;

            if (result.next()) {
                String lastID = result.getString("restaurant_location_ID");
                String[] parts = lastID.split("_");
                if (parts.length == 2) {
                    nextNumber = Integer.parseInt(parts[1]) + 1;
                }
            }

            return String.format("%s%05d", prefix, nextNumber);

        } catch (SQLException e) {
            System.out.println("Error getting restaurant location ID: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    // Insert restaurant location into the database
    public static void insertRestaurantLocation(String restaurantLocationID, String city, String address, String zip) {
        getInstance();

        String query = "INSERT INTO restaurant_location (restaurant_location_ID, city, address, zip_code) VALUES (?, ?, ?, ?)";

        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, restaurantLocationID);
            pstmt.setString(2, city);
            pstmt.setString(3, address);
            pstmt.setString(4, zip);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error inserting restaurant location: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Generate restaurant ID
    public static String generateRestaurantID(String restaurantLocationID) {
        getInstance();

        String prefix = "R_" + restaurantLocationID + "_";

        String query = "SELECT restaurant_ID FROM restaurant WHERE restaurant_ID LIKE ? ORDER BY restaurant_ID DESC LIMIT 1";

        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, prefix + "%");
            ResultSet result = pstmt.executeQuery();

            int nextNumber = 1;

            if (result.next()) {
                String lastID = result.getString("restaurant_ID");
                String[] parts = lastID.split("_");
                if (parts.length == 3) {
                    nextNumber = Integer.parseInt(parts[2]) + 1;
                }
            }

            return String.format("%s%03d", prefix, nextNumber);

        } catch (SQLException e) {
            System.out.println("Error generating restaurant ID: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    // Insert restaurant into the database
    public static void insertRestaurant(String restaurantID, String restaurantLocationID, String companyName, String headerPath) {
        getInstance();

        String query = "INSERT INTO restaurant (restaurant_ID, restaurant_location_ID, restaurant_name, restaurant_header_path) VALUES (?, ?, ?, ?)";

        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, restaurantID);
            pstmt.setString(2, restaurantLocationID);
            pstmt.setString(3, companyName);
            pstmt.setString(4, headerPath);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error inserting restaurant: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Generate business owner ID
    public static String generateBusinessOwnerID(String restaurantID) {
        getInstance();

        String prefix = "BO_" + restaurantID + "_";

        String query = "SELECT business_owner_ID FROM business_owner WHERE business_owner_ID LIKE ? ORDER BY business_owner_ID DESC LIMIT 1";

        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, prefix + "%");
            ResultSet result = pstmt.executeQuery();

            int nextNumber = 1;

            if (result.next()) {
                String lastID = result.getString("business_owner_ID");
                String[] parts = lastID.split("_");
                if (parts.length == 3) {
                    nextNumber = Integer.parseInt(parts[2]) + 1;
                }
            }

            return String.format("%s%03d", prefix, nextNumber);

        } catch (SQLException e) {
            System.out.println("Error generating business owner ID: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    // Insert business owner into the database
    public static void insertBusinessOwner(String ownerID, String restaurantID, String firstName, String lastName, String email, String password) {
        getInstance();

        String query = "INSERT INTO business_owner (business_owner_ID, restaurant_ID, owner_first_name, owner_last_name, owner_email, owner_password) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, ownerID);
            pstmt.setString(2, restaurantID);
            pstmt.setString(3, firstName);
            pstmt.setString(4, lastName);
            pstmt.setString(5, email);
            pstmt.setString(6, password);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error inserting business owner: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Generate a unique product ID
    public static String generateProductID() {
    String prefix = "P";
    String query = "SELECT product_ID FROM product WHERE product_ID LIKE ? ORDER BY product_ID DESC LIMIT 1";

    try (Connection conn = getDBConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        pstmt.setString(1, prefix + "%");
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            String lastID = rs.getString("product_ID"); // e.g. P00001
            int num = Integer.parseInt(lastID.substring(prefix.length())); // parse number part
            num++; // increment
            return String.format("%s%05d", prefix, num); // e.g. P00002
        } else {
            return prefix + "00001"; // first ID if none exist
        }

    } catch (SQLException e) {
        e.printStackTrace();
        return null;
    }
    
    }

    // Insert product into the database
    public static boolean insertProduct(String productID, String restaurantID, String productName, String productDescription, int productQuantity, double productPrice, String productImagePath) {
    String query = "INSERT INTO product " + "(product_ID, restaurant_ID, product_name, product_desc, product_quantity, product_price, product_image_path) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = getDBConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        pstmt.setString(1, productID);
        pstmt.setString(2, restaurantID);
        pstmt.setString(3, productName);
        pstmt.setString(4, productDescription);
        pstmt.setInt(5, productQuantity);
        pstmt.setDouble(6, productPrice);
        pstmt.setString(7, productImagePath);

        return pstmt.executeUpdate() > 0;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }

    }

    // Get business owner ID by email
    public static String getBusinessOwnerID(String email) {
        getInstance();

        String query = "SELECT business_owner_ID FROM business_owner WHERE owner_email = ?";

        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
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

    // Get restaurant name by restaurant ID
    public static String getRestaurantName(String restaurantID) {
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

    // Get restaurant ID by business owner email
    public static String getRestaurantID(String email) {
        getInstance();

        String query = "SELECT restaurant_ID FROM business_owner WHERE owner_email = ?";

        try (Connection conn = getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                return result.getString("restaurant_ID");
            }

        } catch (SQLException e) {
            System.out.println("Error getting restaurant ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Get products by restaurant ID
    public static List<Product> getProductsByRestaurantID(String restaurantID) {
    List<Product> products = new ArrayList<>();
    String query = "SELECT * FROM product WHERE restaurant_ID = ?";

    try (Connection conn = getDBConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        pstmt.setString(1, restaurantID);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            Product product = new Product(
                rs.getString("product_ID"),
                rs.getString("restaurant_ID"),
                rs.getString("product_name"),
                rs.getString("product_desc"),
                rs.getInt("product_quantity"),
                rs.getDouble("product_price"),
                rs.getString("product_image_path")
            );
            products.add(product);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return products;
}

    // Update product name by product ID
    public static boolean updateProductName(String productID, String productName, String productDescription) {
    String query = "UPDATE product SET product_name = ?, product_desc = ? WHERE product_ID = ?";

    try (Connection conn = getDBConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        pstmt.setString(1, productName);
        pstmt.setString(2, productDescription);
        pstmt.setString(3, productID);
        return pstmt.executeUpdate() > 0;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

    public static boolean deleteProductByID(String productID) {
    String query = "DELETE FROM product WHERE product_ID = ?";

    try (Connection conn = getDBConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        pstmt.setString(1, productID);
        return pstmt.executeUpdate() > 0;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

    public static boolean updateProductQuantity(String productId, int productQuantity) {
    String query = "UPDATE product SET product_quantity = ? WHERE product_ID = ?";

    try (Connection conn = getDBConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        pstmt.setInt(1, productQuantity);
        pstmt.setString(2, productId);
        return pstmt.executeUpdate() > 0;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

    public static boolean updateProductPrice(String productId, double productPrice) {
    String query = "UPDATE product SET product_price = ? WHERE product_ID = ?";

    try (Connection conn = getDBConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setDouble(1, productPrice);
        pstmt.setString(2, productId);
        return pstmt.executeUpdate() > 0;
        
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}


}