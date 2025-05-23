package Customer;

import Database.DatabaseCredentials;
import java.sql.*;

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

    // Method to validate either email and password of a customer
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

    // Method to validate email address because it needs to be unique
    public static boolean validateUniqueEmail(String email) {
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

    // Method to validate phone number because it needs to be unique
    public static boolean validateUniquePhoneNumber(String phoneNumber) {
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
}