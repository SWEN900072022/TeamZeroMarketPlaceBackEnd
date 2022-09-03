package UnitofWork;

import Entity.FixedPriceListing;
import Entity.Listing;
import Entity.User;
import Enums.UnitActions;
import Mapper.FixedPriceListingMapper;
import Mapper.ListingMapper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class ListingRepository implements IUnitofWork<Listing>{
    private final Map<String, List<Listing>> fixedPriceContext;
    private final Map<String, List<Listing>> auctionPriceContext;
    private final ListingMapper lMapper;
    private final FixedPriceListingMapper fplMapper;

    public ListingRepository() {
        fixedPriceContext = new HashMap<>();
        auctionPriceContext = new HashMap<>();
        lMapper = new ListingMapper();
        fplMapper = new FixedPriceListingMapper();
    }

    public ListingRepository(ListingMapper lMapper, FixedPriceListingMapper fplMapper) {
        this.lMapper = lMapper;
        this.fplMapper = fplMapper;
        fixedPriceContext = new HashMap<>();
        auctionPriceContext = new HashMap<>();
    }

    @Override
    public void registerNew(Listing listing) {
        register(listing, UnitActions.INSERT.toString());
    }

    @Override
    public void registerModified(Listing listing) {
        register(listing, UnitActions.MODIFY.toString());
    }

    @Override
    public void registerDeleted(Listing listing) {
        register(listing, UnitActions.DELETE.toString());
    }

    private void register(Listing listing, String operation) {
        int type = listing.getType();
        if (type == 0) {
            register(listing, operation, fixedPriceContext);
        } else {
            register(listing, operation, auctionPriceContext);
        }
    }

    private void register(Listing listing, String operation, Map<String, List<Listing>> context) {
        List<Listing> listingToBeRegistered = context.get(operation);
        if(listingToBeRegistered == null) {
            listingToBeRegistered = new ArrayList<>();
        }
        listingToBeRegistered.add(listing);
        context.put(operation, listingToBeRegistered);
    }

    @Override
    public void commit() throws Exception {
        commit(fixedPriceContext);
        commit(auctionPriceContext);
    }

    public void commit(Map<String, List<Listing>> context) throws Exception {
        if(context.size() == 0) {
            return;
        }

        if (context.containsKey(UnitActions.INSERT.toString())) {
            commitNew(context);
        }

        if(context.containsKey(UnitActions.MODIFY.toString())) {
            commitModify(context);
        }

        if(context.containsKey(UnitActions.DELETE.toString())) {
            commitDel(context);
        }
    }

    private void commitNew(Map<String, List<Listing>> context) throws Exception {
        List<Listing> listingList = context.get(UnitActions.INSERT.toString());
        listingList = lMapper.insertWithResultSet(listingList);

        if(listingList.isEmpty()) {
            // Something went wrong here
            throw new Exception();
        }

        // Add them into the respective databases, fixed vs auction
        // Check the type of the first element to see the type of context
        // then, we write to the database
        int type = listingList.get(0).getType();
        if(type == 0) {
            // Write to the fixed price database
            List<FixedPriceListing> fpListingList = listingList.stream()
                                                                .filter(FixedPriceListing.class::isInstance)
                                                                .map(FixedPriceListing.class::cast)
                                                                .collect(toList());
            boolean canInsert = fplMapper.insert(fpListingList);
            if(!canInsert) {
                // Something went wrong
                throw new Exception();
            }
        } else {
            // Write to the auction price database
            // Not implemented yet
        }
    }

    private void commitModify(Map<String, List<Listing>> context) {
        // Unimplemented
    }

    private void commitDel(Map<String, List<Listing>> context) {
        // Unimplemented
    }
}
