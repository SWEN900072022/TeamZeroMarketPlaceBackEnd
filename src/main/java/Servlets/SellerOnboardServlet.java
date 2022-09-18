package Servlets;

import Entity.User;
import Model.SellerGroupModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
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

        SellerGroupModel sgModel = new SellerGroupModel();
        boolean isSuccessful = sgModel.addSellerToSellerGroup(user, groupName);

        Map<String, Boolean> result = new HashMap<>();
        result.put("result", isSuccessful);
        gson = new Gson();
        String json = gson.toJson(result);

        PrintWriter out = response.getWriter();
        out.println(json);
    }
}
