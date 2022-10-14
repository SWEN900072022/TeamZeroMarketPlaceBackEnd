package Injector.FindConditionInjector;

import Injector.ISQLInjector;

public class FindLastOrderInjector implements ISQLInjector {
    @Override
    public String getSQLQuery() {
        return "SELECT * FROM orders WHERE orderid=(SELECT MAX(orderid) from orders)";
    }
}
