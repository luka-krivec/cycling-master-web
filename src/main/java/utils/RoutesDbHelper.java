package utils;

import org.json.simple.JSONObject;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;

/**
 * Created by Luka on 16.1.2015.
 */
public class RoutesDbHelper {

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

    public JSONObject insertRoute(int id_user, String name, float distance, float average_speed, Date start_time, Date end_time) {
        JSONObject response = new JSONObject();

        Connection conn = null;
        PreparedStatement stmt = null;

        ds = getDataSource();

        String sql = "INSERT INTO Routes (idUser, routeName, distance, averageSpeed, startTime, endTime) " +
                     "VALUES (?,?,?,?,?,?)";

        try {
            conn = ds.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id_user);
            stmt.setString(2, name);
            stmt.setFloat(3, distance);
            stmt.setFloat(4, average_speed);
            stmt.setDate(5, start_time);
            stmt.setDate(6, end_time);

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

    public JSONObject insertRoute(String idFacebook) {
        JSONObject response = new JSONObject();

        Connection conn = null;
        PreparedStatement stmt = null;

        UsersDbHelper usersHelper = new UsersDbHelper();
        int idUser = usersHelper.getUserId(idFacebook);

        ds = getDataSource();

        String sql = "INSERT INTO Routes (idUser) " +
                     "VALUES (?)";

        try {
            conn = ds.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUser);

            int res = stmt.executeUpdate();

            if(res > 0) {
                response.put("success", true);

                // Get ID of inserted record
                ResultSet resKeys = stmt.getGeneratedKeys();
                if (resKeys.next()) {
                    response.put("idRoute", resKeys.getLong(1));
                }
                else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }

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

    public JSONObject updateRoute(int idRoute, String name, float distance, float avgSpeed,
                                  Timestamp startTime, Timestamp endTime) {
        JSONObject response = new JSONObject();

        Connection conn = null;
        PreparedStatement stmt = null;

        ds = getDataSource();

        boolean hasName = name != null && name.length() > 0;
        boolean hasDistance = distance > 0;
        boolean hasAvgSpeed = avgSpeed > 0;
        boolean hasStartTime = startTime != null;
        boolean hasEndTime = endTime != null;

        String sql =    "UPDATE Routes " +
                        "SET " + (hasName ? "routeName = ?," : " ")
                        + (hasDistance ? "distance = ?,"  : " ")
                        + (hasAvgSpeed ? "averageSpeed = ?," : " ")
                        + (hasStartTime ? "startTime = ?," : " ")
                        + (hasEndTime ? "endTime = ?," : " ");

        sql = sql.substring(0, sql.length()-1); // trim last semicolon
        sql += " WHERE idRoute = ?";

        try {
            conn = ds.getConnection();
            stmt = conn.prepareStatement(sql);

            int currentIndex = 1;

            if(hasName) {
                stmt.setString(currentIndex++, name);
            }

            if(hasDistance) {
                stmt.setFloat(currentIndex++, distance);
            }

            if(hasAvgSpeed) {
                stmt.setFloat(currentIndex++, avgSpeed);
            }

            if(hasStartTime) {
                stmt.setTimestamp(currentIndex++, startTime);
            }

            if(hasEndTime) {
                stmt.setTimestamp(currentIndex++, endTime);
            }

            stmt.setInt(currentIndex, idRoute);

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
}
