package Injector.FindConditionInjector;

import Injector.IInjector;

public class FindGroupIdByNameInjector implements IInjector {
    @Override
    public String getSQLQuery() {
        return "SELECT * FROM sellergroups WHERE groupname=? LIMIT ? OFFSET ?;";
    }
}
