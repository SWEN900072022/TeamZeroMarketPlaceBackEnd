package Servlets;

import Entity.Order;
import Entity.OrderItem;
import Model.OrderModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.org.apache.xpath.internal.operations.Or;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

@WebServlet(name = "ModifyServlet", value = "/ModifyServlet")
public class ModifyServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderItemsToBeRefactored = request.getParameter("orderItems");
        String jwt = request.getHeader("jwt");

        Type typeOfOrderItem = TypeToken.getParameterized(List.class, OrderItem.class).getType();

        Gson gson = new Gson();
        List<OrderItem>ordersToBeModifiedList = gson.fromJson(orderItemsToBeRefactored, typeOfOrderItem);

        // Perform the modification
        OrderModel om = new OrderModel();
        om.modifyOrders(ordersToBeModifiedList, jwt);
    }
}
