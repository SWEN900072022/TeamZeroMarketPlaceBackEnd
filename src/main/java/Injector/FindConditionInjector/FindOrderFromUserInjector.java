package Injector.FindConditionInjector;

import Injector.ISQLInjector;

public class FindOrderFromUserInjector implements ISQLInjector {
    @Override
    public String getSQLQuery() {
        return "SELECT oi.* FROM orderitems oi " +
                "JOIN orders o on oi.orderid = o.orderid " +
                "WHERE userid=?";
    }
}
