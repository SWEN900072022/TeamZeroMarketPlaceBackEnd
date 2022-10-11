package Servlets;

import Entity.SellerGroup;
import Model.SellerGroupModel;
import UnitofWork.IUnitofWork;
import UnitofWork.UnitofWork;
import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "GetAllSellerGroupServlet", value = "/GetAllSellerGroupServlet")
public class GetAllSellerGroupServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        IUnitofWork repo = new UnitofWork();
        SellerGroupModel sgModel = new SellerGroupModel(repo);
        List<SellerGroup> list;
        list = sgModel.getAllSellerGroup();

        Gson gson = new Gson();
        String json = gson.toJson(list);

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        System.out.println(json);
        out.print(json);
        out.flush();
    }
}
