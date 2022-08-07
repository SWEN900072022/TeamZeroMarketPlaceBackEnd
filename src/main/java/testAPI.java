import com.google.gson.Gson;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

@WebServlet(name = "testAPI", value = "/testAPI")
public class testAPI extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ArrayList<String> results = Query();

        System.out.println("reached");
        Gson gson = new Gson();
        String json = gson.toJson(results);
        System.out.println(json);

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        out.println(json);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private final String url = getEnvValue("DATABASE_URL");
    private final String user = getEnvValue("DATABASE_USER");
    private final String password = getEnvValue("DATABASE_PASSWORD");
    public Connection Connection() {
        Connection conn = null;
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            conn = DriverManager.getConnection(url, user, password);
        } catch(SQLException e) {
            System.out.println(e);
        }
        return conn;
    }

    public ArrayList<String> Query() {
        String sql = "SELECT * FROM test";
        PreparedStatement findStatement = null;
        ResultSet rs = null;
        Connection conn = null;
        ArrayList<String> list = new ArrayList<String>();

        try {
            conn = Connection();

            findStatement = conn.prepareStatement(sql);
            findStatement.execute();

            rs = findStatement.getResultSet();
            while(rs.next()) {
                String output = "";
                output += rs.getInt("column1");
                output += " ";
                output += rs.getString("column2");
                output += " ";
                output += rs.getInt("column3");
                list.add(output);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public String getEnvValue(String s) {
        try {
            return Dotenv.configure().load().get(s);
        } catch (DotenvException e) {
            return System.getenv().get(s);
        } catch (Exception e) {
            System.out.println("Cannot get environment value");
        }
        return "";
    }
}