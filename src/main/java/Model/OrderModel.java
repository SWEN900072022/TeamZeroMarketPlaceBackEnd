package Model;

import Entity.FixedPriceListing;
import Entity.Listing;
import Entity.Order;
import Enums.ListingTypes;
import UnitofWork.IUnitofWork;
import UnitofWork.ListingRepository;
import UnitofWork.OrderRepository;

import java.util.List;
import java.util.Map;

public class OrderModel {
    private IUnitofWork<Order> orderRepo;
    private IUnitofWork<Listing> listingRepo;

    public OrderModel() {
        orderRepo = new OrderRepository();
        listingRepo = new ListingRepository();
    }

    public OrderModel(IUnitofWork<Order> orderRepo, IUnitofWork<Listing> listingRepo) {
        this.orderRepo = orderRepo;
        this.listingRepo = listingRepo;
    }

    public boolean createOrders(Integer[] listingId, Integer[] quantity) {
        // First we validate that the quantity is sufficient
        Map<Integer,Listing> listingMap  = listingRepo.read(listingId);


        // Iterate the listing and check if there are sufficient
        for(int i = 0; i < listingId.length; i++) {
            Listing listing = listingMap.get(listingId[i]);

            // Check quantity if it is fixed price
            // Check highest bidder id if auction
            if(listing.getType() == ListingTypes.FIXED_PRICE) {
                // Fixed price
                FixedPriceListing fpListing = (FixedPriceListing) listing;
                fpListing.load();
                if(fpListing.getQuantity() < quantity[i]) {
                    // There isn't enough to support to order, abort transaction
                    return false;
                }
            } else {
                // Auction
                // Not implemented
            }
        }

        // At this point, we should have enough to satisfy the order.
        // We subtract the quantity from the listing and create a new entry into the order
        // database

        // Order written to database, transaction completed
        return false;
    }
}
