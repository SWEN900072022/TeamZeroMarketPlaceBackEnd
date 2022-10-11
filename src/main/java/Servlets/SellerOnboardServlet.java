package Servlets;

import Entity.User;
import Model.SellerGroupModel;
import UnitofWork.IUnitofWork;
import UnitofWork.UnitofWork;
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

@WebServlet(name = "SellerOnboardServlet", value = "/SellerOnboardServlet")
public class SellerOnboardServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userStr = request.getParameter("user");
        String groupName = request.getParameter("groupName");
        String jwt = request.getHeader("jwt");

        Type typeOfUser = TypeToken.getParameterized(User.class).getType();

        Gson gson = new Gson();
        User user = gson.fromJson(userStr, typeOfUser);

        IUnitofWork repo = new UnitofWork();
        SellerGroupModel sgModel = new SellerGroupModel(repo);
        boolean isSuccessful = sgModel.addSellerToSellerGroup(user, groupName);

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
        gson = new Gson();
        String json = gson.toJson(result);

        PrintWriter out = response.getWriter();
        out.println(json);
    }
}
