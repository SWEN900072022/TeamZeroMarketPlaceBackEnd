package Servlets;

import Entity.Bid;
import JsonDeserializer.BidDeserializer;
import Model.BidModel;
import UnitofWork.IUnitofWork;
import UnitofWork.UnitofWork;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "BidServlet", value = "/BidServlet")
public class BidServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Should get a bid object, verify that the bid listing is active and submit bid
        String bid = request.getParameter("bid");
        String jwt = request.getHeader("jwt");

        // Deserialisers for the json objects
        GsonBuilder gsonBuilder = new GsonBuilder();
        BidDeserializer bidDeserializer = new BidDeserializer();
        gsonBuilder.registerTypeAdapter(Bid.class, bidDeserializer);
        Gson gson = gsonBuilder.create();
        Bid bidObj = gson.fromJson(bid, Bid.class);

        // Business logic initialisation
        // Initialise unit of work and insert them on model initialisation
        IUnitofWork repo = new UnitofWork();
        BidModel bm = new BidModel(repo);
        boolean isSuccessful = bm.createBid(bidObj, jwt);

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

        Map<String, Boolean>result = new HashMap<>();
        result.put("result", isSuccessful);
        gson = new Gson();
        String json = gson.toJson(result);

        PrintWriter out = response.getWriter();
        out.println(json);
    }
}
