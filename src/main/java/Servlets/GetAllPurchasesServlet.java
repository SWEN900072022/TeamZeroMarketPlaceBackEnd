package Servlets;

import Entity.OrderItem;
import Model.OrderModel;
import com.google.gson.Gson;

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
        OrderModel om = new OrderModel();
        List<OrderItem> list = om.getAllOrderItem();

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
