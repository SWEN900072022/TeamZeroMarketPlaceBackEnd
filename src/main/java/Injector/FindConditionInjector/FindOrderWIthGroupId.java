package Injector.FindConditionInjector;

import Injector.IInjector;

public class FindOrderWIthGroupId implements IInjector {
    @Override
    public String getSQLQuery() {
        return "SELECT oi.* FROM orderitems oi " +
                "JOIN listings l on oi.listingid=l.listingid " +
                "where orderid=?";
    }
}
