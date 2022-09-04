package Servlets;

import Entity.FixedPriceListingImpl;
import Entity.Listing;
import Model.ListingModel;
import Util.JWTUtil;
import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "CreateListingServlet", value = "/createListing")
public class CreateListingServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        int type = Integer.parseInt(request.getParameter("type"));
        String jwt = request.getHeader("jwt");

        if(type == 0) {
            int price = Integer.parseInt(request.getParameter("price"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            boolean isSuccessful = false;

            try {
                int createdById = Integer.parseInt(JWTUtil.getSubject(jwt));
                Listing listing = new FixedPriceListingImpl(description, title,createdById, price, quantity);
                ListingModel listingModel = new ListingModel();
                isSuccessful = listingModel.createListing(listing, jwt);
            } catch (NumberFormatException e) {
                isSuccessful = false;
            }

            Map<String, Boolean> result = new HashMap<>();
            result.put("result", isSuccessful);
            Gson gson = new Gson();
            String json = gson.toJson(result);

            PrintWriter out = response.getWriter();
            out.println(json);
        } else {
            // Unimplemented
        }
    }
}
