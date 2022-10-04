package Injector.FindConditionInjector;

import Injector.ISQLInjector;

public class FindGroupIdByNameInjector implements ISQLInjector {
    @Override
    public String getSQLQuery() {
        return "SELECT * FROM sellergroups WHERE groupname=?;";
    }
}
