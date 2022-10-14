package Servlets;

import Domain.Admin;
import Domain.Customer;
import Domain.OrderItem;
import Domain.User;
import Enums.UserRoles;
import JsonSerializer.MoneySerializer;
import UnitofWork.IUnitofWork;
import UnitofWork.UnitofWork;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.javamoney.moneta.Money;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "GetAllPurchasesServlet", value = "/getAllPurchases")
public class GetAllPurchasesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        IUnitofWork repo = new UnitofWork();

        Admin admin = (Admin) User.create("", "", "", 0, UserRoles.ADMIN.toString());
        admin.setRepo(repo);
        List<OrderItem> list = admin.getAllPurchases();

        GsonBuilder gb = new GsonBuilder();
        gb.registerTypeAdapter(Money.class, new MoneySerializer());
        Gson gson = gb.create();
        String json = gson.toJson(list);

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        System.out.println(json);
        out.print(json);
        out.flush();

    }
}
