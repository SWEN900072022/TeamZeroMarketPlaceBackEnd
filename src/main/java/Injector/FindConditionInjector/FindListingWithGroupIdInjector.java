package Injector.FindConditionInjector;

import Injector.ISQLInjector;

public class FindListingWithGroupIdInjector implements ISQLInjector {
    @Override
    public String getSQLQuery() {
        return "SELECT * FROM listings " +
                "WHERE groupid=?;";
    }
}
