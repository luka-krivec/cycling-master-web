package utils;

import org.json.simple.JSONObject;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;


public class UsersDbHelper {

    public static String LAST_ERROR = "";

    private DataSource ds;

    public DataSource getDataSource() {
        if(this.ds != null) {
            return ds;
        }

        try {
            // Obtain our environment naming context
            Context initCtx = new InitialContext();
            Context envCtx = null;
            envCtx = (Context) initCtx.lookup("java:comp/env");

            // Look up our data source
            ds = (DataSource)
                    envCtx.lookup("jdbc/OpenShiftMySQL");
        } catch (NamingException e) {
            e.printStackTrace();
        }

        return ds;
    }

    public ResultObject userNameExists(String username) {
        boolean result = false;
        String error = "";

        Connection conn = null;
        PreparedStatement stmt = null;

        ds = getDataSource();

        String sql = "SELECT idUser " +
                "FROM Users " +
                "WHERE username = ?";

        try {
            conn = ds.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);

            ResultSet resultSet = stmt.executeQuery();

            if(resultSet.next()) {
                result = true;
            }
        } catch (SQLException se) {
            error = se.getMessage();
            se.printStackTrace();
        } catch (Exception e) {
            error = e.getMessage();
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                error = se.getMessage();
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                error = se.getMessage();
                se.printStackTrace();
            }
        }

        LAST_ERROR = error;
        ResultObject res = new ResultObject(result, error);

        return res;
    }

    public ResultObject userExists(String email) {
        boolean result = false;
        String error = "";

        Connection conn = null;
        PreparedStatement stmt = null;

        ds = getDataSource();

        String sql = "SELECT idUser " +
                "FROM Users " +
                "WHERE email = ?";

        try {
            conn = ds.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);

            ResultSet resultSet = stmt.executeQuery();

            if(resultSet.next()) {
                result = true;
            }
        } catch (SQLException se) {
            error = se.getMessage();
            se.printStackTrace();
        } catch (Exception e) {
            error = e.getMessage();
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                error = se.getMessage();
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                error = se.getMessage();
                se.printStackTrace();
            }
        }

        LAST_ERROR = error;
        ResultObject res = new ResultObject(result, error);

        return res;
    }

    public int getUserId(String idFacebook) {
        int result = -1;
        String error = "";

        Connection conn = null;
        PreparedStatement stmt = null;

        ds = getDataSource();

        String sql = "SELECT idUser " +
                     "FROM Users " +
                     "WHERE idFacebook = ?";

        try {
            conn = ds.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, idFacebook);

            ResultSet resultSet = stmt.executeQuery();

            if(resultSet.next()) {
                result = resultSet.getInt("idUser");
            }
        } catch (SQLException se) {
            LAST_ERROR = se.getMessage();
            se.printStackTrace();
        } catch (Exception e) {
            LAST_ERROR = e.getMessage();
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                LAST_ERROR = se.getMessage();
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                LAST_ERROR = se.getMessage();
                se.printStackTrace();
            }
        }

        return  result;
    }

    public JSONObject insertUser(String username, String email, String pass, String authType) {
        JSONObject response = new JSONObject();

        // Check username
        ResultObject resUserNameExists = userNameExists(username);
        if(resUserNameExists.getError().length() == 0) {
            // Username already taken
            if(resUserNameExists.isResultSuccessful()) {
                response.put("error", true);
                response.put("errorCode", 1);
                response.put("errorDescription", "Username is already taken");
                return response;
            }
        } else {
            // Error recieving usernames
            response.put("error", true);
            response.put("errorCode", 3);
            response.put("errorDescription", resUserNameExists.getError());
            return response;
        }


        // Check email
        ResultObject resEmailExists = userExists(email);
        if(resEmailExists.getError().length() == 0) {
            // Email already registered
            if(resEmailExists.isResultSuccessful()) {
                response.put("error", true);
                response.put("errorCode", 2);
                response.put("errorDescription", "Email already registered.");
                return response;
            }
        } else {
            // Error recieving emails
            response.put("error", true);
            response.put("errorCode", 3);
            response.put("errorDescription", resEmailExists.getError());
            return response;
        }

        Connection conn = null;
        PreparedStatement stmt = null;

        ds = getDataSource();

        String sql = "INSERT INTO Users (email, password, userName, authType) " +
                     "VALUES (?,?,?,?)";

        try {
            conn = ds.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, PasswordHash.createHash(pass));
            stmt.setString(3, username);
            stmt.setString(4, authType);

            int res = stmt.executeUpdate();

            if(res > 0) {
                response.put("success", true);
            } else {
                response.put("error", true);
                response.put("errorCode", 3);
                response.put("errorDescription", "Insert failed");
            }

        } catch (SQLException se) {
            LAST_ERROR = se.getMessage();

            response.put("error", true);
            response.put("errorCode", 4);
            response.put("errorDescription", se.getMessage());

            se.printStackTrace();
        } catch (Exception e) {
            LAST_ERROR = e.getMessage();

            response.put("error", true);
            response.put("errorCode", 4);
            response.put("errorDescription", e.getMessage());

            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                LAST_ERROR = se.getMessage();
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                LAST_ERROR = se.getMessage();

                se.printStackTrace();
            }
        }

        return response;
    }

    public JSONObject insertFacebookUser(String idFacebook, String username, String birthday) {
        JSONObject response = new JSONObject();

        // Check username
        ResultObject resUserNameExists = userNameExists(username);
        if(resUserNameExists.getError().length() == 0) {
            // Username already taken
            if(resUserNameExists.isResultSuccessful()) {
                response.put("error", true);
                response.put("errorCode", 1);
                response.put("errorDescription", "Username is already taken");
                return response;
            }
        } else {
            // Error recieving usernames
            response.put("error", true);
            response.put("errorCode", 3);
            response.put("errorDescription", resUserNameExists.getError());
            return response;
        }

        Connection conn = null;
        PreparedStatement stmt = null;

        ds = getDataSource();

        String sql = "INSERT INTO Users (idFacebook, userName, birthday) " +
                     "VALUES (?,?,?)";

        try {
            conn = ds.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, idFacebook);
            stmt.setString(2, username);
            if(birthday != null && !birthday.equals("null")) {
                stmt.setDate(3, new java.sql.Date(new java.util.Date(birthday).getTime()));
            } else {
                stmt.setNull(3, Types.DATE);
            }
            int res = stmt.executeUpdate();

            if(res > 0) {
                response.put("success", true);
            } else {
                response.put("error", true);
                response.put("errorCode", 3);
                response.put("errorDescription", "Insert failed");
            }

        } catch (SQLException se) {
            LAST_ERROR = se.getMessage();

            response.put("error", true);
            response.put("errorCode", 4);
            response.put("errorDescription", se.getMessage());

            se.printStackTrace();
        } catch (Exception e) {
            LAST_ERROR = e.getMessage();

            response.put("error", true);
            response.put("errorCode", 4);
            response.put("errorDescription", e.getMessage());

            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                LAST_ERROR = se.getMessage();
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                LAST_ERROR = se.getMessage();

                se.printStackTrace();
            }
        }

        return response;
    }

    public JSONObject loginUser(String email, String pass) {
        JSONObject response = new JSONObject();

        // Check email
        ResultObject resEmailExists = userExists(email);
        if(resEmailExists.getError().length() == 0) {
            // Email already registered
            if(resEmailExists.isResultSuccessful() == false) {
                response.put("error", true);
                response.put("errorCode", 1);
                response.put("errorDescription", "User with this email not exists");
                return response;
            }
        } else {
            // Error recieving emails
            response.put("error", true);
            response.put("errorCode", 3);
            response.put("errorDescription", resEmailExists.getError());
            return response;
        }

        Connection conn = null;
        PreparedStatement stmt = null;

        ds = getDataSource();

        String passwordFromDatabase = "";

        String sql = "SELECT password " +
                     "FROM Users " +
                     "WHERE email = ?";

        try {
            conn = ds.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);

            ResultSet resultSet = stmt.executeQuery();

            if(resultSet.next()) {
                passwordFromDatabase = resultSet.getString(1);
            }

            boolean validatePassword = PasswordHash.validatePassword(pass, passwordFromDatabase);

            if(validatePassword) {
                response.put("success", true);
            } else {
                response.put("error", true);
                response.put("errorCode", 2);
                response.put("errorDescription", "Wrong password");
            }

        } catch (SQLException se) {
            LAST_ERROR = se.getMessage();

            response.put("error", true);
            response.put("errorCode", 3);
            response.put("errorDescription", se.getMessage());

            se.printStackTrace();
        } catch (Exception e) {
            response.put("error", true);
            response.put("errorCode", 3);
            response.put("errorDescription", e.getMessage());

            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                LAST_ERROR = se.getMessage();
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                LAST_ERROR = se.getMessage();
                se.printStackTrace();
            }
        }

        return response;
    }
}
