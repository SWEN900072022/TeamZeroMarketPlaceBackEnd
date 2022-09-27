package Injector.FindConditionInjector;

import Injector.ISQLInjector;

public class FindOrderForSellerGroupInjector implements ISQLInjector {
    @Override
    public String getSQLQuery() {
        return "SELECT oi.* FROM orderitems oi " +
                " JOIN listings l on oi.listingid = l.listingid " +
                " JOIN groupmembership g on l.groupid = g.groupid " +
                " WHERE userid=?";
    }
}
