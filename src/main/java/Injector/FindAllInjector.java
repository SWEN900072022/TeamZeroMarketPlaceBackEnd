package Injector;

public class FindAllInjector implements FindConditionInjector{
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
            case "listing":
                return getAllListingSQLQuery();
            case "fixed_price_listing":
                return getAllFPListingSQLQuery();
        }
        return "";
    }

    private String getAllUserSQLQuery() {
        return "SELECT * FROM users;";
    }

    private String getAllOrdersSQLQuery() {
        return "SELECT * FROM orders;";
    }

    private String getAllListingSQLQuery() {
        return "SELECT * FROM listing;";
    }

    private String getAllFPListingSQLQuery() {
        return "SELECT * FROM fixed_price_listing;";
    }
}

