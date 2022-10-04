package Injector.FindConditionInjector;

import Injector.ISQLInjector;

public class FindGroupNameInListing implements ISQLInjector {
    @Override
    public String getSQLQuery() {
        return "SELECT l.* FROM listings l " +
                " JOIN sellergroups ON l.groupid = sellergroups.groupid " +
                "WHERE groupname=?;";
    }
}
