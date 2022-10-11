package Injector.FindConditionInjector;

import Injector.ISQLInjector;

public class FindOrderItemWithOrderIdAndListingIdInjector implements ISQLInjector {
    @Override
    public String getSQLQuery() {
        return "SELECT * FROM orderitems WHERE orderid=? and listingid=?;";
    }
}
