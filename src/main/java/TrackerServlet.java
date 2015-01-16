import org.json.simple.JSONObject;
import utils.TrackerDbHelper;
import utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name = "TrackerServlet", urlPatterns = {"/tracker/*"})
public class TrackerServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json; charset=UTF-8");

        TrackerDbHelper dbUtils = new TrackerDbHelper();

        String paramIdRoute = request.getParameter("idRoute");
        String paramLat = request.getParameter("lat");
        String paramLng = request.getParameter("lng");
        String paramAltitude = request.getParameter("altitude");

        int idRoute = 0;
        float lat = 0;
        float lng = 0;
        float altitude = 0;

        boolean parametersSuitable = true;

        if(paramIdRoute != null && Utils.isInteger(paramIdRoute)) {
            idRoute = Integer.parseInt(paramIdRoute);
        } else {
            parametersSuitable = false;
        }

        if(paramLat != null && Utils.isFloat(paramLat)) {
            lat = Float.parseFloat(paramLat);
        } else {
            parametersSuitable = false;
        }

        if(paramLng != null && Utils.isFloat(paramLng)) {
            lng = Float.parseFloat(paramLng);
        } else {
            parametersSuitable = false;
        }

        if(paramAltitude != null && Utils.isFloat(paramAltitude)) {
            altitude = Float.parseFloat(paramAltitude);
        } else {
            parametersSuitable = false;
        }


        if(parametersSuitable) {
            JSONObject responseObject = dbUtils.insertPoint(idRoute, lat, lng, altitude);
            out.println(responseObject);
        } else {
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("error", true);
            responseJSON.put("errorCode", 0);
            responseJSON.put("errorDescription", "Not suitable data");
            out.println(responseJSON);
        }

        out.flush();
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
