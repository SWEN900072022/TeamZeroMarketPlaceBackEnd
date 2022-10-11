package Injector.FindConditionInjector;

import Injector.ISQLInjector;

public class FindBidFromListing implements ISQLInjector {
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
