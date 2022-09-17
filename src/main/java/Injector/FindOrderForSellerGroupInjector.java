package Injector;

public class FindOrderForSellerGroupInjector implements FindConditionInjector{
    @Override
    public String getSQLQuery() {
        return "SELECT * FROM orderitems oi " +
                " JOIN listings l on oi.listingid = l.listingid " +
                " JOIN groupmembership g on l.groupid = g.groupid " +
                " WHERE userid=?";
    }
}
