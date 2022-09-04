package Servlets;

import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CheckoutServlet", value = "/checkout")
public class CheckoutServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String quantity = request.getParameter("quantity");
        String listingId = request.getParameter("listing_id");

        Gson gson = new Gson();
        Integer[] quantityList = gson.fromJson(quantity, Integer[].class);
        Integer[] listingIdList = gson.fromJson(listingId, Integer[].class);
        // Note when handling the list, make sure that the order is not disrupted


    }
}
