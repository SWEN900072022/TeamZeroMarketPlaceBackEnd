package Servlets;

import Entity.Bid;
import Model.BidModel;
import com.google.gson.Gson;
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

        Type TypeOfBid = TypeToken.getParameterized(Bid.class).getType();

        Gson gson = new Gson();
        Bid bidObj = gson.fromJson(bid, TypeOfBid);

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
