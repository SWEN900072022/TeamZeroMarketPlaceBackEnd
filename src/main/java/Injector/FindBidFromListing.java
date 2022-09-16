package Injector;

public class FindBidFromListing implements FindConditionInjector{
    @Override
    public String getSQLQuery() {
        return "SELECT * " +
                "FROM bids " +
                "WHERE bidamount= " +
                "      (SELECT MAX(bidamount) " +
                "       FROM bids " +
                "       WHERE listingid=?) " +
                "AND listingid=?;";
    }
}
