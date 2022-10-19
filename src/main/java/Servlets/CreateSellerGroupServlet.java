package Servlets;

import Domain.Admin;
import Domain.SellerGroup;
import Domain.User;
import Enums.UserRoles;
import PessimisticLock.LockManager;
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

@WebServlet(name = "CreateSellerGroupServlet", value = "/CreateSellerGroupServlet")
public class CreateSellerGroupServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // When creating a seller group, the main thing that is needed is the group name
        String jwt = request.getHeader("jwt");
        String groupName = request.getParameter("groupName");

        UnitofWork repo = new UnitofWork();
        boolean isSuccessful = false;

        try {
            if(JWTUtil.validateToken(jwt)) {
                String role = JWTUtil.getClaim("role", jwt);
                int uid = Integer.parseInt(JWTUtil.getSubject(jwt));

                if (Objects.equals(role, UserRoles.ADMIN.toString())) {
                    Admin admin = (Admin) User.create("", "", "", uid, UserRoles.ADMIN.toString());
                    admin.setRepo(repo);

                    // Lock group name
                    LockManager.getInstance().acquireLock(groupName, "sellergroup", jwt);
                    SellerGroup newSellerGroup = admin.createSellerGroup(groupName);

                    // Once the seller group is created, register it
                    if(newSellerGroup != null) {
                        repo.registerNew(newSellerGroup);
                        isSuccessful = true;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Something went wrong");
        }

        // Check to see if the operations have been successful, if it is committed
        try {
            repo.commit();
        } catch (Exception e) {
        }

        // Release locks here
        LockManager.getInstance().releaseOwner(jwt);

        Map<String, Boolean> result = new HashMap<>();
        result.put("result", isSuccessful);
        Gson gson = new Gson();
        String json = gson.toJson(result);

        PrintWriter out = response.getWriter();
        out.println(json);
    }
}
