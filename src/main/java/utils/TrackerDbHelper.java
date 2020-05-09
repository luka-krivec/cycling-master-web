package utils;

import com.mongodb.*;
import org.json.simple.JSONObject;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TrackerDbHelper {

    public static String LAST_ERROR = "";
    public static MongoDBHelper mongoDBHelper;

    private DataSource ds;

    private String dbUrl;

    public TrackerDbHelper() {
        dbUrl = System.getenv("JDBC_DATABASE_URL");
    }

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
                    envCtx.lookup("jdbc/DB4FreeMySQL");
        } catch (NamingException e) {
            e.printStackTrace();
        }

        return ds;
    }

    /**
     * Delete all route points.
     *
     * @param idRoute
     * @return
     */
    public JSONObject deletePoints(int idRoute) {
        JSONObject response = new JSONObject();

        PreparedStatement stmt = null;

        //ds = getDataSource();

        String sql = "DELETE FROM livetracker.points " +
                "WHERE idRoute = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl)) {

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idRoute);

            int res = stmt.executeUpdate();

            if (res > 0) {
                response.put("success", true);
            } else {
                response.put("error", true);
                response.put("errorCode", 3);
                response.put("errorDescription", "Delete failed");
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
        }

        return response;
    }

    public JSONObject insertPoints(int id_route, String lats, String lons) {
        JSONObject response = new JSONObject();

        float[] lat = getFloats(lats);
        float[] lon = getFloats(lons);

        try {
            mongoDBHelper = new MongoDBHelper();
            DBCollection dbPoints = mongoDBHelper.getCollection("points");
            List<DBObject> points = new ArrayList<DBObject>();

            for(int i = 0; i < lat.length; i++) {
                DBObject point = new BasicDBObject("route_id", id_route)
                        .append("lat", lat[i])
                        .append("lon", lon[i])
                        .append("time", System.currentTimeMillis() / 1000L);
                points.add(point);
            }
            dbPoints.insert(points);
            response.put("success", true);

        } catch (UnknownHostException e) {
            e.printStackTrace();
            response.put("error", true);
            response.put("errorDescription", e.getMessage());
        } finally {
            mongoDBHelper.destroy();
        }

        return response;
    }

    public JSONObject getPoints(int id_route, long timestamp) {
        QueryBuilder queryBuilder = QueryBuilder.start().and(new BasicDBObject("route_id", id_route),
                new BasicDBObject("time", new BasicDBObject("$gt", timestamp)));
        JSONObject response = new JSONObject();
        try {
            mongoDBHelper = new MongoDBHelper();
            DBCollection dbPoints = mongoDBHelper.getCollection("points");
            DBCursor cursor = dbPoints.find(queryBuilder.get());
            List<DBObject> points = new ArrayList<DBObject>();

            try {
                while(cursor.hasNext()) {
                    points.add(cursor.next());
                }
            } finally {
                cursor.close();
            }
            response.put("success", true);
            response.put("points", points);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            response.put("error", true);
            response.put("errorDescription", e.getMessage());
        } finally {
            mongoDBHelper.destroy();
        }

        return response;
    }

    private float[] getFloats(String data) {
        String[] tmp = data.split(",");
        float[] floats = new float[tmp.length];
        for(int i = 0; i < tmp.length; i++) {
            floats[i] = Float.parseFloat(tmp[i]);
        }
        return floats;
    }


    public JSONObject insertPoint(int id_route, float lat, float lng, boolean hasAltitude, float altitude,
                                  boolean hasAccuracy, float accuracy, boolean hasSpeed,  float speed,
                                  boolean hasBearing, float bearing) {
        JSONObject response = new JSONObject();

        PreparedStatement stmt = null;

        //ds = getDataSource();

        String sql = "INSERT INTO livetracker.points (idRoute, lat, lng, altitude, accuracy, speed, bearing) " +
                     "VALUES (?,?,?,?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id_route);
            stmt.setFloat(2, lat);
            stmt.setFloat(3, lng);

            if(hasAltitude) {
                stmt.setFloat(4, altitude);
            } else {
                stmt.setNull(4, Types.FLOAT);
            }

            if(hasAccuracy) {
                stmt.setFloat(5, accuracy);
            } else {
                stmt.setNull(5, Types.FLOAT);
            }

            if(hasSpeed) {
                stmt.setFloat(6, speed);
            } else {
                stmt.setNull(6, Types.FLOAT);
            }

            if(hasBearing) {
                stmt.setFloat(7, bearing);
            } else {
                stmt.setNull(7, Types.FLOAT);
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
        }

        return response;
    }
}
