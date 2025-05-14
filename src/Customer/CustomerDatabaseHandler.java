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

    // Method to validate either mobile number and password
    // or the email and password

    public static boolean validateLoginCredentials(String email_address, String phone_number) {
        getInstance();
        String query = "SELECT * FROM customer WHERE email_address ='" + email_address + "and phone_number = '" + phone_number + "'";
        
        ResultSet result = handler.execQuery(query);

        try {
            if (result.next()) {
                return true;
            }
        } catch (SQLException e) { 
            e.printStackTrace();
        }
        return false;
    }



}