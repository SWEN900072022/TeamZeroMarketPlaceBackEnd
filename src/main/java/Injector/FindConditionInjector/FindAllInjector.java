package Injector.FindConditionInjector;

import Injector.IInjector;

public class FindAllInjector implements IInjector {
    private String tableName;
    public FindAllInjector(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String getSQLQuery() {
        switch(this.tableName) {
            case "users":
                return getAllUserSQLQuery();
            case "orders":
                return getAllOrdersSQLQuery();
            case "listings":
                return getAllListingSQLQuery();
            case "orderitems":
                return getAllOrderItemsSQLQuery();
        }
        return "";
    }

    private String getAllOrderItemsSQLQuery() {
        return "SELECT * FROM orderitems;";
    }

    private String getAllUserSQLQuery() {
        return "SELECT * FROM users;";
    }

    private String getAllOrdersSQLQuery() {
        return "SELECT * FROM orders;";
    }

    private String getAllListingSQLQuery() {
        return "SELECT * FROM listings;";
    }
}

