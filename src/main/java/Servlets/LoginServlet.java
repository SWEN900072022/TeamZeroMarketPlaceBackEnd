package Servlets;

import Entity.User;
import Model.UserModel;
import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String email = request.getParameter("email");
//        String password = request.getParameter("password");
//        String role = request.getParameter("role");
//
//        User user = new User(email, null, password, role);
//        UserModel uModel = new UserModel();
//        String jwt = uModel.login(user);
//
//        Map<String, String> result = new HashMap<>();
//        result.put("jwt", jwt);
//        Gson gson = new Gson();
//        String json = gson.toJson(result);
//
//        PrintWriter out = response.getWriter();
//        out.println(json);
    }
}
