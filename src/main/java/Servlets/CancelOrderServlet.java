package Servlets;

import Entity.Order;
import Model.OrderModel;
import UnitofWork.IUnitofWork;
import UnitofWork.UnitofWork;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        IUnitofWork repo = new UnitofWork();
        OrderModel om = new OrderModel(repo);
        boolean isSuccessful = om.cancelOrders(ordersToBeDeletedList, jwt);

        // Check to see if the operations have been successful, if it is commit
        if(isSuccessful) {
            try {
                repo.commit();
            } catch (SQLException e) {
                repo.rollback();
            }
        } else {
            repo.rollback();
        }

        Map<String, Boolean> result = new HashMap<>();
        result.put("result", isSuccessful);
        gson = new Gson();
        String json = gson.toJson(result);

        PrintWriter out = response.getWriter();
        out.println(json);
    }
}
