package Injector.FindConditionInjector;

import Injector.IInjector;

public class FindBidFromListing implements IInjector {
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
