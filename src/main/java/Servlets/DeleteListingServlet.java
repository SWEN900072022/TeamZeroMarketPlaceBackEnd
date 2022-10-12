package Servlets;

import Domain.Seller;
import Domain.User;
import Enums.UserRoles;
import UnitofWork.IUnitofWork;
import UnitofWork.UnitofWork;
import Util.JWTUtil;
import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@WebServlet(name = "DeleteListingServlet", value = "/DeleteListingServlet")
public class DeleteListingServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Should be able to get a listing id
        Integer listingId = Integer.parseInt(request.getParameter("listingId"));
        String jwt = request.getHeader("jwt");

        // We have the listingId to identify the listing in question, now we will delete the listing
        IUnitofWork repo = new UnitofWork();
        boolean isSuccessful = false;

        try {
            if(JWTUtil.validateToken(jwt)) {
                String role = JWTUtil.getClaim("role", jwt);
                int uid = Integer.parseInt(JWTUtil.getSubject(jwt));
                // Admin and seller can remove listings
                if(Objects.equals(role, UserRoles.SELLER.toString())) {
                    // TODO: add seller remove listing function here
                    Seller seller = (Seller) User.create("", "", "", uid, UserRoles.SELLER.toString());

                }

                if(Objects.equals(role, UserRoles.ADMIN.toString())) {
                    // TODO: add admin remove listing function here
                }

                isSuccessful = true;
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

        Map<String, Boolean> result = new HashMap<>();
        result.put("result", isSuccessful);
        Gson gson = new Gson();
        String json = gson.toJson(result);

        PrintWriter out = response.getWriter();
        out.println(json);
    }
}
