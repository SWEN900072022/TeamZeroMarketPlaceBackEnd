package Model;

import Entity.Listing;
import Mapper.ListingMapper;
import Mapper.Mapper;
import Mapper.FixedPriceListingMapper;
import UnitofWork.IUnitofWork;
import UnitofWork.ListingRepository;
import Util.JWTUtil;

import java.io.UnsupportedEncodingException;

public class ListingModel {
    private final Mapper<Listing> lMapper;
    private IUnitofWork<Listing> repo;
    public ListingModel() {
        // Create a mapper for the model to write data to
        lMapper = new ListingMapper();
        repo = new ListingRepository();
    }

    public ListingModel(IUnitofWork<Listing> repo) {
        this.lMapper = new ListingMapper();
        this.repo = repo;
    }

    public boolean createListing(Listing listing, String jwt) {
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

        repo.registerNew(listing);
        try{
            repo.commit();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
