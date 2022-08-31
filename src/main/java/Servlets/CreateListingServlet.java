package Servlets;

import Entity.FixedPriceListing;
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

@WebServlet(name = "CreateListingServlet", value = "/createListing")
public class CreateListingServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        int type = Integer.parseInt(request.getParameter("type"));
        String jwt = request.getHeader("jwt");

        // Check if the jwt token is valid, if not, return an empty response
        if(!JWTUtil.validateToken(jwt)) {
            PrintWriter out = response.getWriter();
            out.println(new Gson().toJson(new HashMap<>())); //  empty set
        }

        if(type == 0) {
            int price = Integer.parseInt(request.getParameter("price"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            Listing listing = new FixedPriceListing(description, title, price, quantity);
            ListingModel listingModel = new ListingModel();
            listingModel.createListing(listing);
        }
    }
}
