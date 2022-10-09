package Servlets;

import Entity.Order;
import Entity.OrderItem;
import Model.ListingModel;
import Model.OrderModel;
import UnitofWork.IUnitofWork;
import UnitofWork.Repository;
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

@WebServlet(name = "ModifyServlet", value = "/ModifyServlet")
public class ModifyServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderItemsToBeRefactored = request.getParameter("orderItems");
        String jwt = request.getHeader("jwt");

        Type typeOfOrderItem = TypeToken.getParameterized(List.class, OrderItem.class).getType();

        Gson gson = new Gson();
        List<OrderItem>ordersToBeModifiedList = gson.fromJson(orderItemsToBeRefactored, typeOfOrderItem);

        // Perform the modification on the orders
        IUnitofWork repo = new Repository();
        OrderModel om = new OrderModel(repo);
        boolean isSuccessful = om.modifyOrders(ordersToBeModifiedList, jwt);

        if(isSuccessful) {
            for (OrderItem oi : ordersToBeModifiedList) {
                // Perform the modification on the lsiting to update the stock level
                ListingModel lm = new ListingModel(repo);
                isSuccessful = isSuccessful && (lm.modifyListing(oi.getListingId(), oi.getQuantity(), jwt));
            }
        }

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
        String json = gson.toJson(result);

        PrintWriter out = response.getWriter();
        out.println(json);
    }
}
