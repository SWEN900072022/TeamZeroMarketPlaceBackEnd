package Servlets;

import Entity.Listing;
import JsonSerializer.LocalDateTimeSerializer;
import JsonSerializer.MoneySerializer;
import Model.ListingModel;
import UnitofWork.IUnitofWork;
import UnitofWork.Repository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.javamoney.moneta.Money;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet(name = "SearchServlet", value = "/search")
public class SearchServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String filterList = request.getParameter("filter");
        String jwt = request.getHeader("jwt");

        Type typeOfFilter = TypeToken.getParameterized(List.class, Entity.Filter.class).getType();

        Gson gson = new Gson();
        List<Entity.Filter> filterConditions = gson.fromJson(filterList, typeOfFilter);

        IUnitofWork repo = new Repository();
        ListingModel lModel = new ListingModel(repo);
        List<Listing> list = lModel.search(filterConditions, jwt);

        GsonBuilder gb = new GsonBuilder();
        gb.registerTypeAdapter(Money.class, new MoneySerializer());
        gb.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        gson = gb.create();
        String json = gson.toJson(list);

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        System.out.println(json);
        out.print(json);
        out.flush();
    }
}
