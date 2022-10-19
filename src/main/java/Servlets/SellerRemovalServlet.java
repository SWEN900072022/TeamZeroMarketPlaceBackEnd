package Servlets;

import Domain.Admin;
import Domain.GroupMembership;
import Domain.Seller;
import Domain.User;
import Enums.UserRoles;
import UnitofWork.IUnitofWork;
import UnitofWork.UnitofWork;
import Util.JWTUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@WebServlet(name = "SellerRemovalServlet", value = "/SellerRemovalServlet")
public class SellerRemovalServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userStr = request.getParameter("user");
        String groupName = request.getParameter("groupName");
        String jwt = request.getHeader("jwt"); // This is used to authenticate the admin, we will skip this for now

        Type typeOfUser = TypeToken.getParameterized(Seller.class).getType();

        Gson gson = new Gson();
        User user = gson.fromJson(userStr, typeOfUser);

        IUnitofWork repo = new UnitofWork();
        boolean isSuccessful = false;

        try {
            if(JWTUtil.validateToken(jwt)) {
                String role = JWTUtil.getClaim("role", jwt);
                int uid = Integer.parseInt(JWTUtil.getSubject(jwt));
                if(Objects.equals(role, UserRoles.ADMIN.toString())) {
                    Admin admin = (Admin) User.create("", "", "", uid, UserRoles.ADMIN.toString());
                    admin.setRepo(repo);
                    GroupMembership gm = admin.removeSellerFromGroup(groupName, user.getUserId());
                    gm.markForDelete();
                    repo.registerDeleted(gm);

                    isSuccessful = true;
                }
            }
        } catch (Exception e) {
            System.out.println("Something went wrong");
        }

        // Check to see if the operations have been successful, if it is commit
        try {
            repo.commit();
        } catch (Exception e) {
        }

        Map<String, Boolean> result = new HashMap<>();
        result.put("result", isSuccessful);
        gson = new Gson();
        String json = gson.toJson(result);

        PrintWriter out = response.getWriter();
        out.println(json);
    }
}
