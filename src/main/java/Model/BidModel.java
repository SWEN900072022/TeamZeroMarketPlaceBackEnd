package Model;

import Entity.Bid;
import Entity.Listing;
import Injector.FindBidFromListing;
import Injector.FindIdInjector;
import Mapper.BidMapper;
import Mapper.ListingMapper;
import UnitofWork.IUnitofWork;
import UnitofWork.Repository;
import Util.JWTUtil;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BidModel {
    private IUnitofWork<Listing> listingRepo;
    private IUnitofWork<Bid> bidRepo;

    public BidModel() {
        listingRepo = new Repository<Listing>(new ListingMapper());
        bidRepo = new Repository<Bid>(new BidMapper());
    }

    public boolean createBid(Bid bid, String jwt) {
        // Validate jwt token, check the details of the listing, submit the bid
        // Check to see if the jwt token is valid
        try{
            if(!JWTUtil.validateToken(jwt)) {
                // if not valid, return false
                return false;
            }
        } catch (Exception e) {
            // Something went wrong
            return false;
        }

        // The user has a valid JWT, so we check the details of the bid
        // we want to check the duration
        // we also want to make sure that the bid amount is higher than the
        // highest bid
        List<Object> param = new ArrayList<>();
        param.add(bid.getListingId());

        Listing bidListing = listingRepo.read(new FindIdInjector("listings"), param);
        if(Duration.between(bidListing.getStartTime(), LocalDateTime.now()).getSeconds()
                > Duration.between(bidListing.getStartTime(), bidListing.getEndTime()).getSeconds()
        ) {
            // The deadline has passed, so we return false
            return false;
        }

        // Read the bid database to see if there are any with a higher bid
        param = new ArrayList<>();
        param.add(bid.getListingId());
        param.add(bid.getListingId());

        Bid bid1 = bidRepo.read(new FindBidFromListing(), param);
        if(bid1.getBidAmount().isGreaterThan(bid.getBidAmount())) {
            // The new bid is smaller and is overwritten
            return false;
        }

        // Valid listing and sufficient bid
        // Start writing to the database
        bidRepo.registerNew(bid);

        try {
            bidRepo.commit();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
