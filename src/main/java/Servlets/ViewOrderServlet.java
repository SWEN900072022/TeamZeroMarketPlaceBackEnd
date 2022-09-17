package Servlets;

import JsonSerializer.MoneySerializer;
import Model.OrderModel;
import Entity.OrderItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.javamoney.moneta.Money;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "ViewOrderServlet", value = "/ViewOrderServlet")
public class ViewOrderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String jwt = request.getHeader("jwt");

        // Since in the jwt, we have the user id, that would determine how many orders will be retrieved
        OrderModel oModel = new OrderModel();
        List<OrderItem> list = oModel.getOrderItems(jwt);

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
