package Servlets;

import Domain.*;
import Enums.UserRoles;
import JsonSerializer.MoneySerializer;
import UnitofWork.IUnitofWork;
import UnitofWork.UnitofWork;
import Util.JWTUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.javamoney.moneta.Money;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@WebServlet(name = "ViewOrderServlet", value = "/ViewOrderServlet")
public class ViewOrderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String jwt = request.getHeader("jwt");

        // Since in the jwt, we have the user id, that would determine how many orders will be retrieved
        IUnitofWork repo = new UnitofWork();
        boolean isSuccessful = false;
        List<OrderItem> list = new ArrayList<>();

        try {
            if(JWTUtil.validateToken(jwt)) {
                String role = JWTUtil.getClaim("role", jwt);
                int uid = Integer.parseInt(JWTUtil.getSubject(jwt));
                if(Objects.equals(role, UserRoles.ADMIN.toString())) {
                    Admin admin = (Admin) User.create("", "", "", uid, UserRoles.ADMIN);
                    // TODO: get all orders from admin
                }

                if(Objects.equals(role, UserRoles.CUSTOMER.toString())) {
                    Customer customer = (Customer) User.create("", "", "", uid, UserRoles.CUSTOMER);
                    List<Order> orderList = customer.viewAllOrders();
                    for(Order ord : orderList) {
                        list.addAll(ord.getOrderItemList());
                    }
                }

                if(Objects.equals(role, UserRoles.SELLER.toString())) {
                    Seller seller = (Seller) User.create("", "", "", uid, UserRoles.SELLER);
                    // TODO: get all orders from seller
                }

            }
        } catch (Exception e) {
            System.out.println("Something went wrong");
        }

        GsonBuilder gb = new GsonBuilder();
        gb.registerTypeAdapter(Money.class, new MoneySerializer());
        Gson gson = gb.create();
        String json = gson.toJson(list);

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        System.out.println(json);
        out.print(json);
        out.flush();
    }
}
