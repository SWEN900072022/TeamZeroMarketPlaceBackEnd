package Injector.DeleteConditionInjector;

import Injector.ISQLInjector;

public class DeleteOrderItemByListingAndOrderId implements ISQLInjector {
    @Override
    public String getSQLQuery() {
        return "DELETE FROM orderitems WHERE listingid=? and orderid=?;";
    }
}
