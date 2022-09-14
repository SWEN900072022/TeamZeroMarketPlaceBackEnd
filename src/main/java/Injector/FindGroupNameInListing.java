package Injector;

public class FindGroupNameInListing implements FindConditionInjector{
    @Override
    public String getSQLQuery() {
        return "SELECT * FROM listings" +
                " JOIN sellergroups ON listings.groupid = sellergroups.groupid " +
                "WHERE groupname=?;";
    }
}
