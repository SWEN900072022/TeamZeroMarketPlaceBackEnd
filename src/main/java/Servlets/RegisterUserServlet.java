package Servlets;

import Entity.User;
import Model.UserModel;
import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(name = "RegisterUserServlet", value = "/register")
public class RegisterUserServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("password");
        String password = request.getParameter("email");
        String role = request.getParameter("role");

        User user = new User(email, username, password, role);
        UserModel uModel = new UserModel();
        uModel.register(user);
    }
}
