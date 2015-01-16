import org.json.simple.JSONObject;
import utils.UsersDbHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name = "UsersServlet", urlPatterns = {"/users/*"})
public class UsersServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json; charset=UTF-8");

        UsersDbHelper dbUtils = new UsersDbHelper();

        String paramUserSignUp = request.getParameter("userSignUp");
        String paramUserLogin = request.getParameter("userLogin");
        String paramUserName = request.getParameter("username");
        String paramEmail = request.getParameter("email");
        String paramUserPass = request.getParameter("password");
        String paramUserAuthType = request.getParameter("authType");

        if(paramUserSignUp != null && paramUserSignUp.equals("true")
                && paramEmail != null && paramEmail.length() > 0
                && paramUserPass != null && paramUserPass.length() > 0
                && paramUserAuthType != null && paramUserAuthType.length() > 0) {
            JSONObject responseJSON = dbUtils.insertUser(paramUserName, paramEmail, paramUserPass, paramUserAuthType);
            out.println(responseJSON);
        }
        else if(paramUserLogin != null && paramUserLogin.equals("true")
                && paramEmail != null && paramEmail.length() > 0
                && paramUserPass != null && paramUserPass.length() > 0) {
            JSONObject responseJSON = dbUtils.loginUser(paramEmail, paramUserPass);
            out.println(responseJSON);
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
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        out.println("Pozdravljen svet Luka GET!");
        out.flush();
        out.close();
    }
}
