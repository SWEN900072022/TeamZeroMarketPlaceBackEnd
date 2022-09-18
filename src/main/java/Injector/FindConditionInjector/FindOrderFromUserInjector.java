package Injector.FindConditionInjector;

import Injector.IInjector;

public class FindOrderFromUserInjector implements IInjector {
    @Override
    public String getSQLQuery() {
        return "SELECT oi.* FROM orderitems oi " +
                "JOIN orders o on oi.orderid = o.orderid " +
                "WHERE userid=? LIMIT ? OFFSET ?;";
    }
}
