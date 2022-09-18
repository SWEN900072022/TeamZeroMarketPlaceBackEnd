package Injector.FindConditionInjector;

import Injector.IInjector;

public class FindListingWithGroupIdInjector implements IInjector {
    @Override
    public String getSQLQuery() {
        return "SELECT * FROM listings" +
                "WHERE groupid=? LIMIT ? OFFSET ?;";
    }
}
