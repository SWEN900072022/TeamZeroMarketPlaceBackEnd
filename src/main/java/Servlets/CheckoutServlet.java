package Servlets;

import Entity.Order;
import Entity.OrderItem;
import Model.OrderModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "CheckoutServlet", value = "/checkout")
public class CheckoutServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String items = request.getParameter("orderItems");
        String orderString = request.getParameter("order");
        String jwt = request.getHeader("jwt");

        Type typeOfT = TypeToken.getParameterized(List.class, OrderItem.class).getType();
        Type typeOfOrder = TypeToken.getParameterized(Order.class).getType();

        Gson gson = new Gson();
        List<OrderItem>orderItemList = gson.fromJson(items, typeOfT);
        Order order = gson.fromJson(orderString, typeOfOrder);

        OrderModel orderModel = new OrderModel();
        boolean isSuccessful = orderModel.createOrderItem(orderItemList, order, jwt);
        Map<String, Boolean>result = new HashMap<>();
        result.put("result", isSuccessful);
        String json = gson.toJson(result);

        PrintWriter out = response.getWriter();
        out.println(json);
    }
}
