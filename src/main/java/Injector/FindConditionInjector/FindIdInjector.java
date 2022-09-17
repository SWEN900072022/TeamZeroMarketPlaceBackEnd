package Injector.FindConditionInjector;

import Injector.IInjector;

public class FindIdInjector implements IInjector {
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
            case "groupmembership":
                return getGroupMembershipQuery();
        }
        return "";
    }

    private String getUserSQLQuery() {
        return "SELECT * FROM users where userid=?;";
    }

    private String getOrdersSQLQuery() {
        return "SELECT * FROM orders where orderId=?;";
    }

    private String getGroupMembershipQuery() {
        return "SELECT * FROM groupmembership where userid=?;";
    }

    private String getListingSQLQuery() {
        return "SELECT * FROM listing where listingId=?;";
    }
}
