package Servlets;

import Entity.OrderItem;
import JsonSerializer.MoneySerializer;
import Model.OrderModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.javamoney.moneta.Money;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static Util.Util.getVal;

@WebServlet(name = "GetAllPurchasesServlet", value = "/getAllPurchases")
public class GetAllPurchasesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Result limits
        int limit = getVal(request.getParameter("limit"), 50);
        int offset = getVal(request.getParameter("offset"), 0);

        OrderModel om = new OrderModel();
        List<OrderItem> list = om.getAllOrderItem(limit, offset);

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
