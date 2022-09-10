package UnitofWork;

import Entity.FixedPriceListing;
import Entity.FixedPriceListingImpl;
import Entity.Listing;
import Enums.ListingTypes;
import Enums.UnitActions;
import Mapper.FixedPriceListingMapper;
import Mapper.ListingMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class ListingRepository implements IUnitofWork<Listing>{
    private final Map<String, List<Listing>> fixedPriceContext;
    private final Map<String, List<Listing>> auctionPriceContext;
    private final ListingMapper lMapper;
    private final FixedPriceListingMapper fplMapper;
    private Map<Integer, Listing> listingIdentityMap;

    public ListingRepository() {
        fixedPriceContext = new HashMap<>();
        auctionPriceContext = new HashMap<>();
        listingIdentityMap = new HashMap<>();
        lMapper = new ListingMapper();
        fplMapper = new FixedPriceListingMapper();
    }

    public ListingRepository(ListingMapper lMapper, FixedPriceListingMapper fplMapper) {
        this.lMapper = lMapper;
        this.fplMapper = fplMapper;
        this.listingIdentityMap = new HashMap<>();
        fixedPriceContext = new HashMap<>();
        auctionPriceContext = new HashMap<>();
    }

    @Override
    public Map<Integer, Listing> read(Integer[] idList) {
        // Given the listing id, we want to retrieve listing records from the database
        // We check to see if the records is available in the identity mapping
        Map<Integer, Listing> result = new HashMap<>();

        for(Integer id : idList) {
            // See if the record is available
            if(listingIdentityMap.containsKey(id)) {
                result.put(id, listingIdentityMap.get(id));
            } else {
                Listing listing = lMapper.findById(id);
                result.put(id, listing);
            }
        }

        return result;
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
        ListingTypes type = listing.getType();
        if (type == ListingTypes.FIXED_PRICE) {
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
        List<Listing> result = new ArrayList<>();

        for(Listing listing : listingList) {
            result.add(lMapper.insertWithResultSet(listing));
        }

        if(result.isEmpty()) {
            // Something went wrong here
            throw new Exception();
        }

        // Add them into the respective databases, fixed vs auction
        // Check the type of the first element to see the type of context
        // then, we write to the database
        ListingTypes type = result.get(0).getType();
        if(type == ListingTypes.FIXED_PRICE) {
            // Write to the fixed price database
            List<FixedPriceListing> fpListingList = result.stream()
                                                                .filter(FixedPriceListingImpl.class::isInstance)
                                                                .map(FixedPriceListingImpl.class::cast)
                                                                .collect(toList());

            for(FixedPriceListing fpListing : fpListingList) {
                boolean canInsert = fplMapper.insert(fpListing);
                if(!canInsert) {
                    // Something went wrong
                    throw new Exception();
                }
            }
        } else {
            // Write to the auction price database
            // Not implemented yet
        }
    }

    private void commitModify(Map<String, List<Listing>> context) throws Exception {
        List<Listing> listingList = context.get(UnitActions.MODIFY.toString());

        for(Listing listing : listingList) {
            lMapper.modify(listing);
        }

        // Modify the changes to the respective dbs, fixed price vs auction
        ListingTypes type = listingList.get(0).getType();
        if(type == ListingTypes.FIXED_PRICE) {
            List<FixedPriceListing> fpListingList = listingList.stream()
                                                                .filter(FixedPriceListingImpl.class::isInstance)
                                                                .map(FixedPriceListingImpl.class::cast)
                                                                .collect(toList());

            for(FixedPriceListing fpListing : fpListingList) {
                boolean canModify = fplMapper.modify(fpListing);
                if(!canModify) {
                    throw new Exception();
                }
            }
        } else {
            // Write to the auction database
        }
    }

    private void commitDel(Map<String, List<Listing>> context) {
        // Unimplemented
    }
}
