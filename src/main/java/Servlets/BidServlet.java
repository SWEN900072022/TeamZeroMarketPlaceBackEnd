package Servlets;

import Entity.Bid;
import JsonDeserializer.BidDeserializer;
import Model.BidModel;
import UnitofWork.IUnitofWork;
import UnitofWork.Repository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
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
        IUnitofWork repo = new Repository();
        BidModel bm = new BidModel();
        boolean isSuccessful = bm.createBid(bidObj, jwt);

        Map<String, Boolean>result = new HashMap<>();
        result.put("result", isSuccessful);
        gson = new Gson();
        String json = gson.toJson(result);

        PrintWriter out = response.getWriter();
        out.println(json);
    }
}
