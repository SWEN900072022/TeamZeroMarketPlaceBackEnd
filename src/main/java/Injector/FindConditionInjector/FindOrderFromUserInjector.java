package Injector.FindConditionInjector;

import Injector.IInjector;

public class FindOrderFromUserInjector implements IInjector {
    @Override
    public String getSQLQuery() {
        return "SELECT * FROM orderitems oi " +
                "JOIN orders o on oi.orderid = o.orderid " +
                "WHERE userid=?";
    }
}
