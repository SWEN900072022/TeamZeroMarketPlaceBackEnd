package Servlets;

import Entity.Listing;
import Enums.ListingTypes;
import Model.ListingModel;
import UnitofWork.IUnitofWork;
import UnitofWork.Repository;
import Util.JWTUtil;
import com.google.gson.Gson;
import org.javamoney.moneta.Money;

import javax.money.Monetary;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "CreateListingServlet", value = "/createListing")
public class CreateListingServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String type = request.getParameter("type");
        String jwt = request.getHeader("jwt");
        BigDecimal price = new BigDecimal(request.getParameter("price"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        boolean isSuccessful = false;
        IUnitofWork repo = new Repository();

        try {
            int groupId = Integer.parseInt(JWTUtil.getSubject(jwt));
            LocalDateTime startTime = LocalDateTime.parse(request.getParameter("startTime"), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            LocalDateTime endTime = LocalDateTime.parse(request.getParameter("endTime"), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            Listing listing = new Listing(
                    0,
                    groupId,
                    ListingTypes.fromString(type),
                    title,
                    description,
                    quantity,
                    Money.of(price, Monetary.getCurrency("AUD")),
                    startTime,
                    endTime
            );
            ListingModel listingModel = new ListingModel(repo);
            isSuccessful = listingModel.createListing(listing, jwt);
        } catch (NumberFormatException e) {
            System.out.println("Number exception");
        }

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

        Map<String, Boolean> result = new HashMap<>();
        result.put("result", isSuccessful);
        Gson gson = new Gson();
        String json = gson.toJson(result);

        PrintWriter out = response.getWriter();
        out.println(json);
    }
}
