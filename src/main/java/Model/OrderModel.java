package Model;

import Entity.FixedPriceListing;
import Entity.Listing;
import Entity.Order;
import Enums.ListingTypes;
import UnitofWork.IUnitofWork;
import UnitofWork.ListingRepository;
import UnitofWork.OrderRepository;

import java.util.ArrayList;
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
        List<Listing> modifiedListing = new ArrayList<>(); // FixedPriceListing List

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
                } else {
                    fpListing.setQuantity(fpListing.getQuantity() - quantity[i]);
                }

                listing = (Listing)fpListing;
            } else {
                // Auction
                // Not implemented
            }

            // Register the modified objects and the new order
            listingRepo.registerModified(listing);
            Order order = new Order(listingId[i], quantity[i]);
            orderRepo.registerNew(order);
        }

        // Order written to database, transaction completed
        try{
            listingRepo.commit();
            orderRepo.commit();
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
