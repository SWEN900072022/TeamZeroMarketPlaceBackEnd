package Servlets;

import Domain.*;
import Enums.UserRoles;
import Injector.DeleteConditionInjector.DeleteIdInjector;
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

        IUnitofWork repo = new UnitofWork();
        boolean isSuccessful = false;

        // Get roles from the jwt
        try {
            if(JWTUtil.validateToken(jwt)) {
                String role = JWTUtil.getClaim("role", jwt);
                int uid = Integer.parseInt(JWTUtil.getSubject(jwt));
                if(Objects.equals(role, UserRoles.CUSTOMER.toString())) {
                    Customer customer = (Customer) User.create("", "", "", uid, UserRoles.CUSTOMER.toString());
                    customer.setRepo(repo);
                    for(Order ord : ordersToBeDeletedList) {
                        Order cancOrd = customer.cancelOrder(ord.getOrderId());

                        if(cancOrd != null) {
//                            // We want to register the cancellation
//                            for(OrderItem ordItem : cancOrd.getOrderItemList()) {
//
//                                repo.registerDeleted(ordItem);
//                            }

                            // Register the order to be cancelled
                            cancOrd.markForDelete();
                            repo.registerDeleted(cancOrd);
                        }
                    }
                }

                if(Objects.equals(role, UserRoles.SELLER.toString())) {
                    Seller seller = (Seller) User.create("", "", "", uid, UserRoles.SELLER.toString());
                    int groupId = Integer.parseInt(JWTUtil.getClaim("groupId",jwt));
                    for(Order ord : ordersToBeDeletedList) {

                        // Make sure to register the orderitems and order to be deleted
                        Order cancOrd = seller.cancelOrder(ord.getOrderId(), groupId);
                        if(cancOrd != null){
                            for(OrderItem item: cancOrd.getOrderItemList()){
                                repo.registerDeleted(item);
                            }

                            repo.registerDeleted(cancOrd);
                        }
                    }
                }
                isSuccessful = true;
            }
        } catch (Exception e) {

        }

        try {
            repo.commit();
        } catch (Exception e) {
        }


        Map<String, Boolean> result = new HashMap<>();
        result.put("result", isSuccessful);
        gson = new Gson();
        String json = gson.toJson(result);

        PrintWriter out = response.getWriter();
        out.println(json);
    }
}
