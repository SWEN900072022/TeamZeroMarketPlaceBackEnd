package Model;

import Entity.Listing;
import Entity.Order;
import Enums.ListingTypes;
import Mapper.ListingMapper;
import Mapper.OrderMapper;
import UnitofWork.IUnitofWork;
import UnitofWork.Repository;
import Util.JWTUtil;

import java.util.Map;

public class OrderModel {
    private IUnitofWork<Order> orderRepo;
    private IUnitofWork<Listing> listingRepo;

    public OrderModel() {
        orderRepo = new Repository<Order>(new OrderMapper());
        listingRepo = new Repository<Listing>(new ListingMapper());
    }

    public OrderModel(IUnitofWork<Order> orderRepo, IUnitofWork<Listing> listingRepo) {
        this.orderRepo = orderRepo;
        this.listingRepo = listingRepo;
    }

    public void createOrders() {

    }

//    public boolean createOrders(Integer[] listingId, Integer[] quantity, String jwt) {
//        // Check to see if the jwt token is valid
//        try {
//            if(!JWTUtil.validateToken(jwt)) {
//                return false;
//            }
//        } catch (Exception e) {
//            return false;
//        }
//
//        // First we validate that the quantity is sufficient
//        Map<Integer,Listing> listingMap  = listingRepo.read(listingId, "listing");
//
//        if(listingMap.size() != listingId.length) {
//            // Some listing id is invalid
//            return false;
//        }
//
//        // Iterate the listing and check if there are sufficient
//        for(int i = 0; i < listingId.length; i++) {
//            Listing listing = listingMap.get(listingId[i]);
//
//            // Check quantity if it is fixed price
//            // Check highest bidder id if auction
//            if(listing.getType() == ListingTypes.FIXED_PRICE) {
//                // Fixed price
//                listing.load();
//                if(listing.getQuantity() < quantity[i]) {
//                    // There isn't enough to support to order, abort transaction
//                    return false;
//                } else {
//                    listing.setQuantity(listing.getQuantity() - quantity[i]);
//                }
//            } else if(listing.getType() == ListingTypes.AUCTION) {
//                // Auction
//                // Not implemented
//            }
//
//            // Register the modified objects and the new order
//            listingRepo.registerModified(listing);
//            int userId = Integer.parseInt(JWTUtil.getSubject(jwt));
//            Order order = new Order(listingId[i], quantity[i], userId);
//            orderRepo.registerNew(order);
//        }
//
//        // Order written to database, transaction completed
//        try{
//            listingRepo.commit();
//            orderRepo.commit();
//        } catch (Exception e) {
//            return false;
//        }
//
//        return true;
//    }
}
