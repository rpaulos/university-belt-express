package Database;
import java.sql.*;

public class DatabaseHandler {
    private static DatabaseHandler handler = null;
    private static Statement stmt = null;
    private static PreparedStatement pstatement = null;

    public static String dburl = DatabaseCredentials.ignoreDburl;
    public static String userName = DatabaseCredentials.ignoreUserName;
    public static String password = DatabaseCredentials.ignorePassword;

    public static DatabaseHandler getInstance() {
        if (handler == null) {
            handler = new DatabaseHandler();
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

    public static String getLastName(String phone_number) {
        String query = "SELECT last_name FROM users WHERE phone_number = ?";
        String last_name = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet result = null;

        try {
            conn = getDBConnection();
            stmt = conn.prepareStatement(query);
            stmt.setString(1, phone_number);
            result = stmt.executeQuery();

            if(result.next()) {
                last_name = result.getString("last_name");
            } 
            
        } catch (SQLException e) {
                e.printStackTrace();
            }
        return last_name;
    }
}
