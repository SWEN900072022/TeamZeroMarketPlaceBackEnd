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
            case "sellergroup":
                return getSellerGroupMembershipQuery();
        }
        return "";
    }

    private String getSellerGroupMembershipQuery() {
        return "SELECT * FROM sellergroups where groupid=? LIMIT ? OFFSET?;";
    }

    private String getUserSQLQuery() {
        return "SELECT * FROM users where userid=? LIMIT ? OFFSET ?;";
    }

    private String getOrdersSQLQuery() {
        return "SELECT * FROM orders where orderId=? LIMIT ? OFFSET ?;";
    }

    private String getGroupMembershipQuery() {
        return "SELECT * FROM groupmembership where userid=? LIMIT ? OFFSET ?;";
    }

    private String getListingSQLQuery() {
        return "SELECT * FROM listings where listingId=? LIMIT ? OFFSET ?;";
    }
}
