package Servlets;

import Domain.User;
import PessimisticLock.LockManager;
import UnitofWork.IUnitofWork;
import UnitofWork.UnitofWork;
import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "RegisterUserServlet", value = "/user/register")
public class RegisterUserServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        IUnitofWork repo = new UnitofWork();
        boolean isSuccessful = false;

        try {
            LockManager.getInstance().acquireLock(email, "users", email);
            User user = User.register(email, username, password, role, repo);

            if(user != null) {
                // Register is successful, user is registered to be commited
                repo.registerNew(user);
                isSuccessful = true;
            }
        } catch (Exception e) {
            System.out.println("Something went wrong");
        }

        try {
            repo.commit();
        } catch (SQLException e) {
            repo.rollback();
        }

        LockManager.getInstance().releaseOwner(email);

        Map<String, Boolean>result = new HashMap<>();
        result.put("result", isSuccessful);
        Gson gson = new Gson();
        String json = gson.toJson(result);

        PrintWriter out = response.getWriter();
        out.println(json);
    }
}
