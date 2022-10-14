package Servlets;

import Domain.Admin;
import Domain.SellerGroup;
import Domain.User;
import Enums.UserRoles;
import UnitofWork.IUnitofWork;
import UnitofWork.UnitofWork;
import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "GetAllUserServlet", value = "/getAllUsers")
public class GetAllUsersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        IUnitofWork repo = new UnitofWork();

        Admin admin = (Admin) User.create("","","", 0, UserRoles.ADMIN.toString());
        admin.setRepo(repo);
        List<User> list = admin.getAllUsers();

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
