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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        boolean isSuccessful = om.modifyOrders(ordersToBeModifiedList, jwt);
        Map<String, Boolean> result = new HashMap<>();
        result.put("result", isSuccessful);
        String json = gson.toJson(result);

        PrintWriter out = response.getWriter();
        out.println(json);
    }
}
