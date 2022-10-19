package Util;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class SQLUtil {
    private static SQLUtil instance;

    private Queue<Connection> connectionQueue;

    public static synchronized SQLUtil getInstance() {
        if(instance == null) {
            instance = new SQLUtil();
        }
        return instance;
    }

    private SQLUtil() {
        connectionQueue = new LinkedList<>();

        for(int i = 0; i < 10; i ++) {
            connectionQueue.add(getNewConnection());
        }
    }

    public synchronized Connection getConnection(){
        while(connectionQueue.size() == 0) {
            try{
                wait(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return connectionQueue.remove();
    }

    public synchronized void close(Connection conn) {
        connectionQueue.add(conn);
        notifyAll();
    }

    public static String getEnvValue(String s) {
        try {
            return Dotenv.configure().load().get(s);
        } catch (DotenvException e) {
            return System.getenv().get(s);
        } catch (Exception e) {
            System.out.println("Cannot get environment value");
        }
        return "";
    }

    public synchronized Connection getNewConnection() {
        Connection conn = null;
        try {
            // This is for production
            DriverManager.registerDriver(new org.postgresql.Driver());
            conn = DriverManager.getConnection(getEnvValue("POSTGRES_URL"));
            conn.setAutoCommit(false);
        } catch(SQLException e) {
            try {
                // This is for dev
                conn = DriverManager.getConnection(
                        getEnvValue("POSTGRES_URL"),
                        getEnvValue("POSTGRES_USER"),
                        getEnvValue("POSTGRES_PASSWORD"));
                conn.setAutoCommit(false);
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        }
        return conn;
    }
}
