package utils;


import java.sql.*;

public class DatabaseUtils {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://127.2.246.2:3306/cyclingmaster";

    //  Database credentials
    static final String USER = "admin7ijNKTX";
    static final String PASS = "Rx1er3VQKjK4";

    public static String LAST_ERROR = "";

    public static boolean insertUser(String username, String email, String pass, String authType) {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stmt = null;

        String sql = "INSERT INTO Users (email, password, username, auth_type) " +
                     "VALUES (?, ?, ?, ?)";

        try {
            Class.forName("com.mysql.jdbc.Driver");

            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, PasswordHash.createHash(pass));
            stmt.setString(3, username);
            stmt.setString(4, authType);

            stmt.executeUpdate();

            result = true;

        } catch (SQLException se) {
            LAST_ERROR = se.getMessage();
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    conn.close();
                }
            } catch (SQLException se) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                LAST_ERROR = se.getMessage();
                se.printStackTrace();
            }
        }

        return result;
    }
}
