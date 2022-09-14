package Servlets;

import Entity.Listing;
import Model.ListingModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.List;

@WebServlet(name = "SearchServlet", value = "/user/search")
public class SearchServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String filterList = request.getParameter("filter");
        String jwt = request.getHeader("jwt");

        Type typeOfFilter = TypeToken.getParameterized(List.class, Filter.class).getType();

        Gson gson = new Gson();
        List<Entity.Filter> filterConditions = gson.fromJson(filterList, typeOfFilter);

        ListingModel lModel = new ListingModel();
        List<Listing> list = lModel.search(filterConditions, jwt);

        String json = gson.toJson(list);

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        System.out.println(json);
        out.print(json);
        out.flush();
    }
}
