package Injector.FindConditionInjector;

import Injector.IInjector;

public class FindOrderItemWithOrderIdAndListingIdInjector implements IInjector {
    @Override
    public String getSQLQuery() {
        return "SELECT * FROM orderitems WHERE orderid=? and listingid=?;";
    }
}
