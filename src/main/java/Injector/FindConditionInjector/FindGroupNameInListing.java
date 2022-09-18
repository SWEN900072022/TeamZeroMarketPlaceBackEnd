package Injector.FindConditionInjector;

import Injector.IInjector;

public class FindGroupNameInListing implements IInjector {
    @Override
    public String getSQLQuery() {
        return "SELECT l.* FROM listings l " +
                " JOIN sellergroups ON l.groupid = sellergroups.groupid " +
                "WHERE groupname=? LIMIT ? OFFSET ?;";
    }
}
