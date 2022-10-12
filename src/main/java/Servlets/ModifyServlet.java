package Servlets;

import Domain.Customer;
import Domain.Order;
import Domain.OrderItem;
import Domain.User;
import Enums.UserRoles;
import UnitofWork.IUnitofWork;
import UnitofWork.UnitofWork;
import Util.JWTUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.*;

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
        IUnitofWork repo = new UnitofWork();
        boolean isSuccessful = false;

        try {
            if(JWTUtil.validateToken(jwt)) {
                String role = JWTUtil.getClaim("role", jwt);
                int uid = Integer.parseInt(JWTUtil.getSubject(jwt));
                List<Order> modifiedOrder = new ArrayList<>();

                if (Objects.equals(role, UserRoles.CUSTOMER.toString())) {
                    Customer customer = (Customer) User.create("", "", "", uid, UserRoles.CUSTOMER.toString());
                    for(OrderItem orderItem : ordersToBeModifiedList) {
                        Order modiOrder = customer.modifyOrder(
                                orderItem.getOrderId(),
                                orderItem.getListingId(),
                                orderItem.getQuantity());

                        if(modiOrder != null) {
                            modifiedOrder.add(modiOrder);
                        }
                    }

                    for(Order order : modifiedOrder) {
                        for(OrderItem ordItem : order.getOrderItemList()) {
                            repo.registerModified(ordItem);
                        }
                    }
                    isSuccessful = true;
                }

                if(Objects.equals(role, UserRoles.SELLER.toString())) {
                    // TODO: add the seller modifying the order
                }

            }
        } catch (Exception e) {
            System.out.print("Something went wrong");
        }

        // Check to see if the operations have been successful, if it is commit
        try {
            repo.commit();
        } catch (SQLException e) {
            repo.rollback();
        }

        Map<String, Boolean> result = new HashMap<>();
        result.put("result", isSuccessful);
        String json = gson.toJson(result);

        PrintWriter out = response.getWriter();
        out.println(json);
    }
}
