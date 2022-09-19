package Servlets;

import Entity.Order;
import Model.OrderModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

@WebServlet(name = "CancelOrderServlet", value = "/CancelOrderServlet")
public class CancelOrderServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Should be able to get an order json object
        String ordersToBeDeleted = request.getParameter("orders");
        String jwt = request.getHeader("jwt");

        Type typeOfOrder = TypeToken.getParameterized(List.class, Order.class).getType();
        Gson gson = new Gson();
        List<Order>ordersToBeDeletedList = gson.fromJson(ordersToBeDeleted, typeOfOrder);

        // Now that we have the list we will proceed with deleting the order
        OrderModel om = new OrderModel();
        om.cancelOrders(ordersToBeDeletedList, jwt);
    }
}
