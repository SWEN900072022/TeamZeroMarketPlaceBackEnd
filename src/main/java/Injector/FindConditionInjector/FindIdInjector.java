package Injector.FindConditionInjector;

public class FindIdInjector extends GeneralFindConditionInjector {
    private String tableName;
    public FindIdInjector(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String getSQLQuery() {
        switch(this.tableName) {
            case "users":
                return getUserSQLQuery();
            case "orders":
                return getOrdersSQLQuery();
            case "listings":
                return getListingSQLQuery();
            case "fixed_price_listing":
                return getFPListingSQLQuery();
        }
        return "";
    }

    private String getUserSQLQuery() {
        return "SELECT * FROM users where userid=?;";
    }

    private String getOrdersSQLQuery() {
        return "SELECT * FROM orders where orderid=?;";
    }

    private String getListingSQLQuery() {
        return "SELECT * FROM listing where id=?;";
    }

    private String getFPListingSQLQuery() {
        return "SELECT * FROM fixed_price_listing where id=?;";
    }
}
