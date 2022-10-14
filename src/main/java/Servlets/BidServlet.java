package Servlets;

import Domain.Bid;
import Domain.Customer;
import Domain.User;
import Enums.UserRoles;
import JsonDeserializer.BidDeserializer;
import UnitofWork.IUnitofWork;
import UnitofWork.UnitofWork;
import Util.JWTUtil;
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
import java.util.Objects;

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

        IUnitofWork repo = new UnitofWork();
        Bid resultBid;
        Boolean isSuccessful = false;

        // Get roles from the jwt
        try {
            if(JWTUtil.validateToken(jwt)) {
                if(Objects.equals(JWTUtil.getClaim("role", jwt), UserRoles.CUSTOMER.toString())) {
                    int uid = Integer.parseInt(JWTUtil.getSubject(jwt));
                    Customer customer = (Customer) User.create("", "", "", uid, UserRoles.CUSTOMER.toString());
                    customer.setRepo(repo);
                    resultBid = customer.bid(bidObj.getListingId(), bidObj.getBidAmount());
                    if(resultBid != null) {
                        repo.registerNew(resultBid);
                        isSuccessful = true;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Something went wrong");
        }

        // Check to see if the operations have been successful, if it is commit
        try {
            repo.commit();
        } catch (SQLException e) {
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
