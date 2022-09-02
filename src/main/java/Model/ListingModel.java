package Model;

import Entity.Listing;
import Mapper.ListingMapper;
import Mapper.Mapper;
import UnitofWork.ListingRepository;

public class ListingModel {
    private final Mapper<Listing> lMapper;
    private ListingRepository repo;
    public ListingModel() {
        // Create a mapper for the model to write data to
        lMapper = new ListingMapper();
        repo = new ListingRepository();
    }

    public ListingModel(Mapper<Listing> mapper) {
        this.lMapper = mapper;
        repo = new ListingRepository();
    }

    public void createListing(Listing listing) {
        repo.registerNew(listing);
        repo.commit();
    }
}
