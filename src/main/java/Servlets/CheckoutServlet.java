package Servlets;

import Domain.*;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

        IUnitofWork repo = new UnitofWork();
        boolean isSuccessful = false;

        // Get roles from the jwt
        try {
            if(JWTUtil.validateToken(jwt)) {
                int uid = Integer.parseInt(JWTUtil.getSubject(jwt));
                if(Objects.equals(JWTUtil.getClaim("role", jwt), UserRoles.CUSTOMER.toString())) {
                    Customer customer = (Customer) User.create("", "", "", uid, UserRoles.CUSTOMER.toString());
                    customer.setRepo(repo);
                    Order newOrder = customer.checkoutListing(order.getAddress(), orderItemList);

                    IUnitofWork repo2 = new UnitofWork();
                    repo2.registerNew(newOrder);
                    repo2.commit();

                    if(newOrder != null) {
                        // We want to register the order items
                        for(OrderItem orderItem : newOrder.getOrderItemList()) {
                            Listing l = Listing.getListingById(orderItem.getListingId(), repo);
                            if(l.getQuantity() > orderItem.getQuantity()) {
                                orderItem.setOrderId(newOrder.getOrderId());
                                l.setQuantity(l.getQuantity() - orderItem.getQuantity());
                                repo.registerNew(orderItem);
                                repo.registerModified(l);
                            }
                        }
                    }
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

        Map<String, Boolean>result = new HashMap<>();
        result.put("result", isSuccessful);
        String json = gson.toJson(result);

        PrintWriter out = response.getWriter();
        out.println(json);
    }
}
