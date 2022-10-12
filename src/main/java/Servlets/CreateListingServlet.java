package Servlets;

import Domain.Customer;
import Domain.Listing;
import Domain.Seller;
import Domain.User;
import Enums.ListingTypes;
import Enums.UserRoles;
import UnitofWork.IUnitofWork;
import UnitofWork.UnitofWork;
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
import java.util.Objects;

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
        IUnitofWork repo = new UnitofWork();

        try {
//            int groupId = Integer.parseInt(JWTUtil.getSubject(jwt));
            LocalDateTime startTime = LocalDateTime.parse(request.getParameter("startTime"), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            LocalDateTime endTime = LocalDateTime.parse(request.getParameter("endTime"), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            if(JWTUtil.validateToken(jwt)) {
                String role = JWTUtil.getClaim("role", jwt);
                int uid = Integer.parseInt(JWTUtil.getSubject(jwt));
                String groupId = Integer.parseInt(JWTUtil.getClaim("groupId"));
                if(Objects.equals(role, UserRoles.SELLER.toString())) {
                    Seller seller = (Seller) User.create("", "", "", uid, UserRoles.SELLER);

                    // TODO: add seller create listing
                    // seller.createListing();

                        Listing newListing = seller.createListing(ListingTypes.fromString(type), title, description, quantity, Money.of(price, Monetary.getCurrency("AUD")), startTime, endTime);
                        repo.registerNew(newListing);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Number exception");
        }

        // Check to see if the operations have been successful, if it is commit
        try {
            repo.commit();
        } catch (SQLException e) {
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
