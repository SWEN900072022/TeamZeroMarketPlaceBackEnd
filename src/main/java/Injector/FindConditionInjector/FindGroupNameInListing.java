package Injector.FindConditionInjector;

import Injector.IInjector;

public class FindGroupNameInListing implements IInjector {
    @Override
    public String getSQLQuery() {
        return "SELECT * FROM listings" +
                " JOIN sellergroups ON listings.groupid = sellergroups.groupid " +
                "WHERE groupname=?;";
    }
}
