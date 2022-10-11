package Injector.FindConditionInjector;

import Injector.ISQLInjector;

public class FindOrderWIthGroupId implements ISQLInjector {
    @Override
    public String getSQLQuery() {
        return "SELECT * FROM orderitems oi " +
                "JOIN listings l on oi.listingid=l.listingid " +
                "where orderid=?";
    }
}
