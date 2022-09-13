package Servlets;

import Entity.User;
import Model.UserModel;
import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
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
//
//        User user = new User(email, username, password, role);
//        UserModel uModel = new UserModel();
//        boolean hasRegistered = uModel.register(user);
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
