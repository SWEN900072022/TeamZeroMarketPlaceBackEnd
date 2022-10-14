package Injector.FindConditionInjector;

import Injector.ISQLInjector;

public class FindOrderItemWithOrderId implements ISQLInjector {
    @Override
    public String getSQLQuery() {
        return "SELECT * FROM orderitems " +
                "WHERE orderid=?;";
    }
}
