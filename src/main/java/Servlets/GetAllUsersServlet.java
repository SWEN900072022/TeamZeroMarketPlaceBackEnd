package Servlets;

import UnitofWork.IUnitofWork;
import UnitofWork.UnitofWork;
import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "GetAllUserServlet", value = "/getAllUsers")
public class GetAllUsersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        IUnitofWork repo = new UnitofWork();

        // TODO: pass JWT Token here
        // TODO: add from admin reading all users
//        UserModel uModel = new UserModel(repo);
//        List<User> list;
//        list = uModel.getAllUsers();
//
//        Gson gson = new Gson();
//        String json = gson.toJson(list);
//
//        PrintWriter out = response.getWriter();
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        System.out.println(json);
//        out.print(json);
//        out.flush();


    }

}
