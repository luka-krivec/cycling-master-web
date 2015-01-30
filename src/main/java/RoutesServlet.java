import org.json.simple.JSONObject;
import utils.RoutesDbHelper;
import utils.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@WebServlet(name = "RoutesServlet", urlPatterns = {"/routes/*"})
public class RoutesServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json; charset=UTF-8");

        RoutesDbHelper dbUtils = new RoutesDbHelper();
        String dateFormatString = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatString);

        String paramInsertRouteAll = request.getParameter("insertRouteAll");
        String paramIdUser = request.getParameter("idUser");
        String paramName = request.getParameter("name");
        String paramDistance= request.getParameter("distance");
        String paramAvgSpeed = request.getParameter("averageSpeed");
        String paramStartTime= request.getParameter("startTime");
        String paramEndTime = request.getParameter("endTime");

        String paramUpdateRoute = request.getParameter("updateRoute");
        String paramIdRoute = request.getParameter("idRoute");

        String paramIdFacebook = request.getParameter("idFacebook");
        String paramInsertNewRoute = request.getParameter("insertNewRoute");

        if(paramInsertRouteAll != null && paramInsertRouteAll.length() > 0) {
            JSONObject responseJSON = insertNewRouteWithAllParameters(paramIdUser, paramName, paramDistance, paramAvgSpeed,
                    paramStartTime, paramEndTime, dateFormatString, dateFormat);
            out.println(responseJSON);
        } else if(paramInsertNewRoute != null && paramInsertNewRoute.length() > 0
                && paramIdFacebook != null && paramIdFacebook.length() > 0) {
            JSONObject responseObject = dbUtils.insertRoute(paramIdFacebook);
            out.println(responseObject);
        }  else if(paramUpdateRoute != null && paramUpdateRoute.length() > 0) {
            JSONObject responseObject = updateRoute(paramIdRoute, paramName, paramDistance, paramAvgSpeed,
                    paramStartTime, paramEndTime, dateFormatString, dateFormat);
            out.println(responseObject);
        } else {
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("error", true);
            responseJSON.put("errorCode", 0);
            responseJSON.put("errorDescription", "Not enough data");
            out.println(responseJSON);
        }



        out.flush();
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private JSONObject updateRoute(String paramIdRoute, String paramName, String paramDistance, String paramAvgSpeed,
                             String paramStartTime, String paramEndTime,
                             String dateFormatString, SimpleDateFormat dateFormat) {

        JSONObject responseJSON = new JSONObject();
        RoutesDbHelper dbUtils = new RoutesDbHelper();
        int idRoute;

        if(paramIdRoute == null || !Utils.isInteger(paramIdRoute)) {
            responseJSON.put("error", true);
            responseJSON.put("errorCode", 0);
            responseJSON.put("errorDescription", "idRoute parameter missing!");
            return responseJSON;
        } else {
            idRoute = Integer.parseInt(paramIdRoute);
        }

        String name = "";
        float distance = 0;
        float avgSpeed = 0;
        Timestamp startTime = null;
        Timestamp endTime = null;

        if(paramName != null && paramName.length() > 0) {
            name = paramName;
        }

        if(paramDistance != null && Utils.isFloat(paramDistance)) {
            distance = Float.parseFloat(paramDistance);
        }

        if(paramAvgSpeed != null && Utils.isFloat(paramAvgSpeed)) {
            avgSpeed = Float.parseFloat(paramAvgSpeed);
        }

        try {
            if (paramStartTime != null && (paramStartTime.length() == dateFormatString.length())) {
                startTime = new java.sql.Timestamp(dateFormat.parse(paramStartTime).getTime());
            }

            if (paramEndTime != null && (paramEndTime.length() == dateFormatString.length())) {
                endTime = new java.sql.Timestamp(dateFormat.parse(paramEndTime).getTime());
            }
        } catch (ParseException pex) {
            responseJSON.put("error", true);
            responseJSON.put("errorCode", 1);
            responseJSON.put("errorDescription", "Date parse exception: " + pex.getMessage());
        }

        // Update route
        responseJSON = dbUtils.updateRoute(idRoute, name, distance, avgSpeed, startTime, endTime);

        return responseJSON;
    }

    private JSONObject insertNewRouteWithAllParameters(String paramIdUser, String paramName, String paramDistance,
                                                 String paramAvgSpeed, String paramStartTime, String paramEndTime,
                                                 String dateFormatString, SimpleDateFormat dateFormat) {

        JSONObject responseJSON = new JSONObject();

        int idUser = 0;
        String name = "";
        float distance = 0;
        float avgSpeed = 0;
        Date startTime = null;
        Date endTime = null;

        RoutesDbHelper dbUtils = new RoutesDbHelper();

        boolean parametersSuitable = true;
        String failedData = "";

        if(paramIdUser != null && Utils.isInteger(paramIdUser)) {
            idUser = Integer.parseInt(paramIdUser);
        } else {
            parametersSuitable = false;
            failedData += "idUser (input:" + paramIdUser + ") ";
        }

        if(paramName != null && paramName.length() > 0) {
            name = paramName;
        } else {
            parametersSuitable = false;
            failedData += "name (input:" + paramName + ") ";
        }

        if(paramDistance != null && Utils.isFloat(paramDistance)) {
            distance = Float.parseFloat(paramDistance);
        } else {
            parametersSuitable = false;
            failedData += "distance (input:" + paramDistance + ") ";
        }

        if(paramAvgSpeed != null && Utils.isFloat(paramAvgSpeed)) {
            avgSpeed = Float.parseFloat(paramAvgSpeed);
        } else {
            parametersSuitable = false;
            failedData += "averageSpeed (input:" + paramAvgSpeed + ") ";
        }

        try {
            if (paramStartTime != null && (paramStartTime.length() == dateFormatString.length())) {
                startTime = new java.sql.Date(dateFormat.parse(paramStartTime).getTime());
            } else {
                parametersSuitable = false;
                failedData += "startTime (input:" + paramStartTime + ") ";
            }

            if (paramEndTime != null && (paramEndTime.length() == dateFormatString.length())) {
                endTime = new java.sql.Date(dateFormat.parse(paramEndTime).getTime());
            } else {
                parametersSuitable = false;
                failedData += "endTime (input:" + paramEndTime + ") ";
            }
        } catch (ParseException pex) {
            responseJSON.put("error", true);
            responseJSON.put("errorCode", 1);
            responseJSON.put("errorDescription", "Date parse exception: " + pex.getMessage());
        }


        if(parametersSuitable) {
            responseJSON = dbUtils.insertRoute(idUser, name, distance, avgSpeed, startTime, endTime);;
        } else {
            responseJSON.put("error", true);
            responseJSON.put("errorCode", 0);
            responseJSON.put("errorDescription", "Not suitable data");
            responseJSON.put("errorDataFields", failedData);
        }

        return responseJSON;
    }
}
