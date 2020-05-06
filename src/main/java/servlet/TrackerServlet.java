package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import utils.MongoDBHelper;
import utils.TrackerDbHelper;
import utils.Utils;

@WebServlet(name = "TrackerServlet", urlPatterns = { "/tracker/*" })
public class TrackerServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json; charset=UTF-8");

        TrackerDbHelper dbUtils = new TrackerDbHelper();

        String paramIdRoute = request.getParameter("idRoute");
        String paramLat = request.getParameter("lat");
        String paramLng = request.getParameter("lng");
        String paramAltitude = request.getParameter("altitude");
        String paramSpeed = request.getParameter("speed");
        String paramBearing = request.getParameter("bearing");
        String paramAccuracy = request.getParameter("accuracy");
        String paramLats = request.getParameter("lats");
        String paramLons = request.getParameter("lons");
        String paramGetPoints = request.getParameter("getPoints");
        String paramTimestamp = request.getParameter("timestamp");

        int idRoute = 0;
        float lat = 0;
        float lng = 0;
        float altitude = 0;
        float accuracy = 0;
        float speed = 0;
        float bearing = 0;
        long timestamp = 0;

        String lats = "";
        String lons = "";

        boolean parametersSuitable = true;
        boolean hasAltitude = false;
        boolean hasSpeed = false;
        boolean hasBearing = false;
        boolean hasAccuracy = false;
        String failedData = "";

        // Required parameters
        if (paramIdRoute != null && Utils.isInteger(paramIdRoute)) {
            idRoute = Integer.parseInt(paramIdRoute);
        } else {
            parametersSuitable = false;
            failedData += "idRoute (input:" + paramIdRoute + ") ";
        }

        // Optional parameters
        if (paramLat != null && Utils.isFloat(paramLat)) {
            lat = Float.parseFloat(paramLat);
        }

        if (paramLng != null && Utils.isFloat(paramLng)) {
            lng = Float.parseFloat(paramLng);
        }

        if (paramLats != null) {
            lats = paramLats;
        }

        if (paramLons != null) {
            lons = paramLons;
        }

        if (paramAltitude != null && Utils.isFloat(paramAltitude)) {
            altitude = Float.parseFloat(paramAltitude);
            hasAltitude = altitude > 0;
        }

        if (paramAccuracy != null && Utils.isFloat(paramAccuracy)) {
            accuracy = Float.parseFloat(paramAccuracy);
            hasAccuracy = accuracy > 0;
        }

        if (paramSpeed != null && Utils.isFloat(paramSpeed)) {
            speed = Float.parseFloat(paramSpeed);
            hasSpeed = speed > 0;
        }

        if (paramBearing != null && Utils.isFloat(paramBearing)) {
            bearing = Float.parseFloat(paramBearing);
            hasBearing = (bearing >= 0) && (bearing <= 360);
        }

        if(paramTimestamp != null) {
            timestamp = Long.parseLong(paramTimestamp);
        }

        if (parametersSuitable) {
            if(paramLat != null && paramLng != null) {
                JSONObject responseObject = dbUtils.insertPoint(idRoute, lat, lng, hasAltitude, altitude,
                        hasAccuracy, accuracy, hasSpeed, speed, hasBearing, bearing);
                out.println(responseObject);
            } else if(paramLats != null && paramLons != null) {
                JSONObject responseObject = dbUtils.insertPoints(idRoute, lats, lons);
                out.println(responseObject);
            } else if(paramGetPoints != null && paramTimestamp != null) {
                JSONObject responseObject = dbUtils.getPoints(idRoute, timestamp);
                out.println(responseObject);
            }
        } else {
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("error", true);
            responseJSON.put("errorCode", 0);
            responseJSON.put("errorDescription", "Not suitable data");
            responseJSON.put("errorDataFields", failedData);
            out.println(responseJSON);
        }

        out.flush();
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
}
