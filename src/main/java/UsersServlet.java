import utils.DatabaseUtils;

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
        String paramUserSignUp = request.getParameter("usersignup");
        String paramUserName = request.getParameter("username");
        String paramEmail = request.getParameter("email");
        String paramUserPass = request.getParameter("pass");
        String paramUserAuthType = request.getParameter("authType");

        boolean userSignUp = false;

        if(paramUserSignUp != null && paramUserSignUp.equals("true")) {
            userSignUp = DatabaseUtils.insertUser(paramUserName, paramEmail, paramUserPass, paramUserAuthType);
        }

        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        out.println(userSignUp);
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
