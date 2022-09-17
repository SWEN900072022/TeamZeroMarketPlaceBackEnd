package Injector.FindConditionInjector;

import Injector.IInjector;

public class FindOrderForSellerGroupInjector implements IInjector {
    @Override
    public String getSQLQuery() {
        return "SELECT * FROM orderitems oi " +
                " JOIN listings l on oi.listingid = l.listingid " +
                " JOIN groupmembership g on l.groupid = g.groupid " +
                " WHERE userid=?";
    }
}
