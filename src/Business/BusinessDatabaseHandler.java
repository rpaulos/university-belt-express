package Business;

import Database.DatabaseCredentials;
import java.sql.*;
import java.time.Year;

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
    public static boolean businessOwnerEmailExists(String oemail) {
        getInstance();

        String query = "SELECT * FROM business_owner WHERE owner_email = ?";
    
        try (Connection conn = getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, oemail);

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

}