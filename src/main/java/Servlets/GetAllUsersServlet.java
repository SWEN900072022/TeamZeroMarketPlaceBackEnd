package Servlets;

import Entity.User;
import Mapper.UserMapper;
import Model.UserModel;
import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static Util.Util.getVal;

@WebServlet(name = "GetAllUserServlet", value = "/getAllUsers")
public class GetAllUsersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Result limits
        int limit = getVal(request.getParameter("limit"), 50);
        int offset = getVal(request.getParameter("offset"), 0);

        UserModel uModel = new UserModel();
        List<User> list;
        list = uModel.getAllUsers(limit, offset);

        Gson gson = new Gson();
        String json = gson.toJson(list);

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        System.out.println(json);
        out.print(json);
        out.flush();


    }

}
