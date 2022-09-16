package Model;

import Entity.Listing;
import Entity.Order;
import Enums.UserRoles;
import Injector.DeleteConditionInjector.DeleteIdInjector;
import Injector.FindConditionInjector.FindIdInjector;
import Injector.FindConditionInjector.FindOrderWIthGroupId;
import Mapper.ListingMapper;
import Mapper.OrderMapper;
import UnitofWork.IUnitofWork;
import UnitofWork.Repository;
import Util.JWTUtil;

import java.util.ArrayList;
import java.util.List;

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

    public boolean cancelOrders(List<Order> ordersToBeDeletedList, String jwt) {
        // Check token, verify that the user can delete the order, delete order
        String role;
        try {
            if (!JWTUtil.validateToken(jwt)) {
                // if not valid, return false
                return false;
            }
            role = JWTUtil.getClaim("role", jwt);
        } catch (Exception e) {
            // Something went wrong
            return false;
        }

        // Users and sellers can only cancel orders that they control admin can remove any
        if (role == null || role == "") {
            return false;
        }

        if (role == UserRoles.CUSTOMER.toString()) {
            // Check to see if the user own the order
            for (Order o : ordersToBeDeletedList) {
                List<Object> param = new ArrayList<>();
                param.add(o.getOrderId());

                Order o1 = orderRepo.read(
                        new FindIdInjector("orders"),
                        param,
                        Integer.toString(o.getOrderId())
                );

                if (o.getUserId() != Integer.parseInt(JWTUtil.getSubject(jwt))) {
                    return false;
                } else {
                    // Register the order to be changed
                    param = new ArrayList<>();
                    param.add(o.getOrderId());

                    o.setInjector(new DeleteIdInjector("orders"));
                    o.setParam(param);
                    orderRepo.registerDeleted(o);
                    // This should remove the ones n orderitems as well since they are foreign keys
                }
            }
        } else if (role == UserRoles.SELLER.toString()) {
            // Check to see if the seller owns the order
            for (Order o : ordersToBeDeletedList) {
                List<Object> param = new ArrayList<>();
                param.add(o.getOrderId());

                Listing l = listingRepo.read(
                        new FindOrderWIthGroupId(),
                        param,
                        Integer.toString(o.getOrderId())
                );

                if (l.getGroupId() != Integer.parseInt(JWTUtil.getClaim("groupId", jwt))) {
                    return false;
                } else {
                    // Register the order to be changed
                    param = new ArrayList<>();
                    param.add(o.getOrderId());

                    o.setInjector(new DeleteIdInjector("orders"));
                    o.setParam(param);
                    orderRepo.registerDeleted(o);
                    // This should remove the ones n orderitems as well since they are foreign keys
                }

            }
        } else if (role == UserRoles.ADMIN.toString()) {
            // Admin can just remove any listing
            for (Order o : ordersToBeDeletedList) {
                List<Object> param = new ArrayList<>();
                param.add(o.getOrderId());

                o.setInjector(new DeleteIdInjector("orders"));
                o.setParam(param);
                orderRepo.registerDeleted(o);
            }
        } else {
            // Unknown role
            return false;
        }
        return true;
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
