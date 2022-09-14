package Model;

import Entity.Listing;
import Entity.Order;
import Entity.OrderItem;
import Entity.User;
import Enums.ListingTypes;
import Enums.UserRoles;
import Injector.FindAllInjector;
import Injector.FindIdInjector;
import Injector.FindOrderForSellerGroupInjector;
import Injector.FindOrderFromUserInjector;
import Mapper.ListingMapper;
import Mapper.OrderItemMapper;
import Mapper.OrderMapper;
import Mapper.UserMapper;
import UnitofWork.IUnitofWork;
import UnitofWork.Repository;
import Util.JWTUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderModel {
    private IUnitofWork<Order> orderRepo;
    private IUnitofWork<OrderItem> orderItemRepo;
    private IUnitofWork<Listing> listingRepo;
    private IUnitofWork<User> userRepo;

    public OrderModel() {
        orderRepo = new Repository<Order>(new OrderMapper());
        listingRepo = new Repository<Listing>(new ListingMapper());
        userRepo = new Repository<User>(new UserMapper());
        orderItemRepo = new Repository<OrderItem>(new OrderItemMapper());
    }

    public OrderModel(IUnitofWork<Order> orderRepo, IUnitofWork<Listing> listingRepo, IUnitofWork<User> userRepo) {
        this.orderRepo = orderRepo;
        this.listingRepo = listingRepo;
        this.userRepo = userRepo;
    }

    public void createOrders() {

    }

    public List<OrderItem> getOrderItems(String jwt) {
        // Check to see if the jwt token is valid
        try{
            if(!JWTUtil.validateToken(jwt)) {
                // if not valid, return false
                return null;
            }
        } catch (Exception e) {
            // Something went wrong
            return null;
        }

        // Depending on the user we will use different injectors
        String userId = JWTUtil.getSubject(jwt);

        // Get the user based on the ID
        List<Object> param = new ArrayList<>();
        param.add(Integer.parseInt(userId));

        User user = userRepo.read(new FindIdInjector("users"), param);
        List<OrderItem> result;

        if(user.getRoleEnum() == UserRoles.CUSTOMER) {
            // Customer
            // Only gets orders from the users
            result = orderItemRepo.readMulti(new FindOrderFromUserInjector(), param);

        } else if (user.getRoleEnum() == UserRoles.SELLER) {
            // Seller
            // Only gets orders from its seller group
            result = orderItemRepo.readMulti(new FindOrderForSellerGroupInjector(), param);
        } else {
            // Admin
            // Gets everything
            result = orderItemRepo.readMulti(new FindAllInjector("orderitems"), new ArrayList<>());
        }
        return result;
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
