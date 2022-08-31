package UnitofWork;

import Entity.Listing;
import Entity.User;
import Enums.UnitActions;
import Mapper.ListingMapper;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListingRepository implements IUnitofWork<Listing>{
    private final Map<String, List<Listing>> fixedPriceContext;
    private final Map<String, List<Listing>> auctionPriceContext;
    private final ListingMapper lMapper;

    public ListingRepository() {
        fixedPriceContext = new HashMap<>();
        auctionPriceContext = new HashMap<>();
        lMapper = new ListingMapper();
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
    public void commit() {
    }

    public void commit(Map<String, List<Listing>> context) {
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

    private void commitNew(Map<String, List<Listing>> context) {
        List<Listing> userList = context.get(UnitActions.INSERT.toString());
        lMapper.insert(userList);
    }

    private void commitModify(Map<String, List<Listing>> context) {
        List<Listing> userList = context.get(UnitActions.MODIFY.toString());
        lMapper.modify(userList);
    }

    private void commitDel(Map<String, List<Listing>> context) {
        List<Listing> userList = context.get(UnitActions.DELETE.toString());
        lMapper.modify(userList);
    }
}
