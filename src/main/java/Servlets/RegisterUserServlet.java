package Servlets;

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

        // TODO: user registration not implemented

//        User user = new User(email, username, password, role);
//        IUnitofWork repo = new UnitofWork();
//        UserModel uModel = new UserModel(repo);
//        boolean hasRegistered = uModel.register(user);
//
//        // Check to see if the operations have been successful, if it is commit
//        if(hasRegistered) {
//            try {
//                repo.commit();
//            } catch (SQLException e) {
//                repo.rollback();
//            }
//        } else {
//            repo.rollback();
//        }
//
//        Map<String, Boolean>result = new HashMap<>();
//        result.put("result", hasRegistered);
//        Gson gson = new Gson();
//        String json = gson.toJson(result);
//
//        PrintWriter out = response.getWriter();
//        out.println(json);
    }
}
