package Injector.DeleteConditionInjector;

import Injector.IInjector;

public class DeleteIdInjector implements IInjector {
    private String tableName;
    public DeleteIdInjector(String tableName) {
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
                return getListingsSQLQuery();
            case "sellergroups":
                return getSellerGroupsSQLQuery();
        }
        return "";
    }

    public String getUserSQLQuery() {
        return "DELETE FROM users WHERE userid=?;";
    }

    public String getOrdersSQLQuery() {
        return "DELETE FROM orders where orderid=?;";
    }

    public String getListingsSQLQuery() {
        return "DELETE FROM listings where listingid=?;";
    }

    public String getSellerGroupsSQLQuery() {
        return "DELETE FROM sellergroups where groupid=?;";
    }
}
