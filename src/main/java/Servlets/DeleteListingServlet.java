package Servlets;

import Model.ListingModel;
import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "DeleteListingServlet", value = "/DeleteListingServlet")
public class DeleteListingServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Should be able to get a listing id
        Integer listingId = Integer.parseInt(request.getParameter("listingId"));
        String jwt = request.getHeader("jwt");

        // We have the listingId to identify the listing in question, now we will delete the listing
        ListingModel listingModel = new ListingModel();
        boolean isSuccessful = listingModel.delete(listingId, jwt);

        Map<String, Boolean> result = new HashMap<>();
        result.put("result", isSuccessful);
        Gson gson = new Gson();
        String json = gson.toJson(result);

        PrintWriter out = response.getWriter();
        out.println(json);
    }
}
