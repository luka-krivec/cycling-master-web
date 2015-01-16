package utils;

import org.json.simple.JSONObject;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class TrackerDbHelper {

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


    public JSONObject insertPoint(int id_route, float lat, float lng, float altitude) {
        JSONObject response = new JSONObject();

        Connection conn = null;
        PreparedStatement stmt = null;

        ds = getDataSource();

        String sql = "INSERT INTO Points (id_route, lat, lng, altitude) " +
                     "VALUES (?,?,?,?)";

        try {
            conn = ds.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id_route);
            stmt.setFloat(2, lat);
            stmt.setFloat(3, lng);
            stmt.setFloat(4, altitude);

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
