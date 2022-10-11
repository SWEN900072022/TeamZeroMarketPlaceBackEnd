package Injector.FindConditionInjector;

import Injector.ISQLInjector;

public class FindOrderWithUser implements ISQLInjector {
    @Override
    public String getSQLQuery() {
        return "SELECT * FROM orders " +
                "WHERE userid=?;";
    }
}
