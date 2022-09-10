package Servlets;

import Model.OrderModel;
import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "CheckoutServlet", value = "/checkout")
public class CheckoutServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String quantity = request.getParameter("quantity");
        String listingId = request.getParameter("listing_id");
        String jwt = request.getHeader("jwt");

        Gson gson = new Gson();
        Integer[] quantityList = gson.fromJson(quantity, Integer[].class);
        Integer[] listingIdList = gson.fromJson(listingId, Integer[].class);
        // Note when handling the list, make sure that the order is not disrupted

        OrderModel orderModel = new OrderModel();
        boolean isSuccessful = orderModel.createOrders(listingIdList, quantityList, jwt);

        Map<String, Boolean>result = new HashMap<>();
        result.put("result", isSuccessful);
        String json = gson.toJson(result);

        PrintWriter out = response.getWriter();
        out.println(json);
    }
}
