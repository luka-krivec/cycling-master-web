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

        String paramIdUser = request.getParameter("idUser");
        String paramName = request.getParameter("name");
        String paramDistance= request.getParameter("distance");
        String paramAvgSpeed = request.getParameter("averageSpeed");
        String paramStartTime= request.getParameter("startTime");
        String paramEndTime = request.getParameter("endTime");

        int idUser = 0;
        String name = "";
        float distance = 0;
        float avgSpeed = 0;
        Date startTime = null;
        Date endTime = null;

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
            JSONObject responseJSON = new JSONObject();
            responseJSON.put("error", true);
            responseJSON.put("errorCode", 1);
            responseJSON.put("errorDescription", "Date parse exception: " + pex.getMessage());
            out.println(responseJSON);
        }


        if(parametersSuitable) {
            JSONObject responseObject = dbUtils.insertRoute(idUser, name, distance, avgSpeed, startTime, endTime);
            out.println(responseObject);
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
