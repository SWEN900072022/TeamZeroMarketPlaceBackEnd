package Servlets;

import Entity.SellerGroup;
import Model.SellerGroupModel;
import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "CreateSellerGroupServlet", value = "/CreateSellerGroupServlet")
public class CreateSellerGroupServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // When creating a seller group, the main thing that is needed is the group name
        String jwt = request.getHeader("jwt");
        String groupName = request.getParameter("groupName");

        SellerGroup sg = new SellerGroup(groupName);
        SellerGroupModel sgModel = new SellerGroupModel();
        boolean hasCreated = sgModel.createSellerGroup(sg, jwt);

        Map<String, Boolean> result = new HashMap<>();
        result.put("result", hasCreated);
        Gson gson = new Gson();
        String json = gson.toJson(result);

        PrintWriter out = response.getWriter();
        out.println(json);
    }
}
