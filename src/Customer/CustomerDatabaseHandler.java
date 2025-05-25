package Customer;

import Database.DatabaseCredentials;
import java.sql.*;
import java.time.Year;

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
    public static String generateNextCustomerID() {
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
}