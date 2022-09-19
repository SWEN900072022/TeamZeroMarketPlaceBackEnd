package Model;

import Entity.Listing;
import Entity.Order;
import Entity.OrderItem;
import Entity.User;
import Enums.UserRoles;
import Injector.DeleteConditionInjector.DeleteIdInjector;
import Injector.FindConditionInjector.*;
import Mapper.ListingMapper;
import Mapper.OrderItemMapper;
import Mapper.OrderMapper;
import Mapper.UserMapper;
import UnitofWork.IUnitofWork;
import UnitofWork.Repository;
import Util.JWTUtil;

import java.util.ArrayList;
import java.util.List;

public class OrderModel {
    private IUnitofWork<Order> orderRepo;
    private IUnitofWork<Listing> listingRepo;
    private IUnitofWork<OrderItem> orderItemRepo;
    private IUnitofWork<User> userRepo;

    public OrderModel() {
        orderRepo = new Repository<Order>(new OrderMapper());
        listingRepo = new Repository<Listing>(new ListingMapper());
        userRepo = new Repository<User>(new UserMapper());
        orderItemRepo = new Repository<OrderItem>(new OrderItemMapper());
    }

    public OrderModel(IUnitofWork<Order> orderRepo, IUnitofWork<Listing> listingRepo, IUnitofWork<OrderItem> orderItemRepo, IUnitofWork<User> userRepo) {
        this.orderRepo = orderRepo;
        this.listingRepo = listingRepo;
        this.userRepo = userRepo;
        this.orderItemRepo = orderItemRepo;
    }

    public List<OrderItem> getAllOrderItem(int limit, int offset) {
        List<Object> param = new ArrayList<>();
        param.add(limit);
        param.add(offset);
        List<OrderItem> orderItemList = orderItemRepo.readMulti(new FindAllInjector("orderitems"), param);
        return orderItemList;
    }

    public boolean createOrderItem(List<OrderItem> orderItemList, Order order, String jwt) {
        // Check to see if the jwt token is valid
        try {
            if (!JWTUtil.validateToken(jwt)) {
                return false;
            } else {
                // Set the user id for the order
                order.setUserId(Integer.parseInt(JWTUtil.getSubject(jwt)));
            }
        } catch (Exception e) {
            return false;
        }

        orderRepo.registerNew(order);
        orderRepo.commit();
        //  Get the orderid before we start inserting the orderitems
        int orderId = OrderMapper.latestKeyVal;

        // First we validate that the quantity is sufficient
        for (OrderItem oi : orderItemList) {
            List<Object> param = new ArrayList<>();
            param.add(oi.getListingId());
            Listing l = listingRepo.read(new FindIdInjector("listings"), param, Integer.toString(oi.getListingId()));
            if (l == null) {
                // The listing does not exist
                return false;
            }

            if (l.getQuantity() < oi.getQuantity()) {
                // There isn't enough to support the order
                return false;
            } else {
                l.setQuantity(l.getQuantity() - oi.getQuantity());
            }

            listingRepo.registerModified(l);
            oi.setOrderId(orderId);
            orderItemRepo.registerNew(oi);
        }
        // Push changes
        try {
            listingRepo.commit();
            orderItemRepo.commit();
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    public List<OrderItem> getOrderItems(int limit, int offset, String jwt) {
        // Check to see if the jwt token is valid
        try {
            if (!JWTUtil.validateToken(jwt)) {
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
        param.add(limit);
        param.add(offset);

        User user = userRepo.read(new FindIdInjector("users"), param);
        List<OrderItem> result;

        if (user.getRoleEnum() == UserRoles.CUSTOMER) {
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
        if (role == null || role.equals("")) {
            return false;
        }

        if (role.equals(UserRoles.CUSTOMER.toString())) {
            // Check to see if the user own the order
            for (Order o : ordersToBeDeletedList) {
                // If the order is null or empty, we skip it
                if(o == null || o.isEmpty()) {
                    continue; // Move on to the next one
                }

                List<Object> param = new ArrayList<>();
                param.add(o.getOrderId());

                Order o1 = orderRepo.read(
                        new FindIdInjector("orders"),
                        param,
                        Integer.toString(o.getOrderId())
                );

                if (o1.getUserId() != Integer.parseInt(JWTUtil.getSubject(jwt))) {
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
        } else if (role.equals(UserRoles.SELLER.toString())) {
            // Check to see if the seller owns the order
            for (Order o : ordersToBeDeletedList) {
                // If the order is null or empty, we skip it
                if(o == null || o.isEmpty()) {
                    continue; // Move on to the next one
                }

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
        } else if (role.equals(UserRoles.ADMIN.toString())) {
            // Admin can just remove any listing
            for (Order o : ordersToBeDeletedList) {
                // If the order is null or empty, we skip it
                if(o == null || o.isEmpty()) {
                    continue; // Move on to the next one
                }

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

        // Commit the changes to the database
        try {
            orderRepo.commit();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean modifyOrders(List<OrderItem> ordersToBeModifiedList, String jwt) {
        // Check token, verify that the user is able to modify the order
        try {
            if(!JWTUtil.validateToken(jwt)) {
                return false;
            } else {

            }
        } catch (Exception e) {
            return false;
        }

        // First need to validate that the user has acccess to the order
        // Once we validated that the user have access to the order, we see if apply the modification
        // Increase/Decrease for users
        // Decrease for sellers
        // Register and commit the changes

        String userId = JWTUtil.getSubject(jwt);

        List<Object> param = new ArrayList<>();
        param.add(Integer.parseInt(userId));

        User user = userRepo.read(new FindIdInjector("users"), param);

        try {
            if(user.getRoleEnum() == UserRoles.CUSTOMER) {
                // Customer
                modifyCustomerOrders(ordersToBeModifiedList);
            } else if(user.getRoleEnum() == UserRoles.SELLER) {
                //Seller
                modifySellerOrders(ordersToBeModifiedList);
            }
        } catch (Exception e) {
            return false;
        }
        // Admin shouldn't be able to modify

        // All operations done
        orderItemRepo.commit();
        return true;
    }

    private void modifyCustomerOrders(List<OrderItem> ordersToBeModifiedList) throws Exception{
        // We don't need to check the quantity here, just check if the order item is null
        for(OrderItem oi : ordersToBeModifiedList) {
            if(oi == null || oi.isEmpty()) {
                throw new Exception();
            }

            // We should have a valid order item now, just register it as modify
            orderItemRepo.registerModified(oi);
        }
    }

    private void modifySellerOrders(List<OrderItem> ordersToBeModifiedList) throws Exception{
        // We need to fetch from the order item table to verify that the quantity is not increased
        for(OrderItem oi : ordersToBeModifiedList) {
            if(oi == null || oi.isEmpty()) {
                throw new Exception();
            }

            // Make sure that the quantity is only reduced
            List<Object>param = new ArrayList<>();
            param.add(oi.getOrderId());
            param.add(oi.getListingId());
            OrderItem oiDB = orderItemRepo.read(new FindOrderItemWithOrderIdAndListingIdInjector(), param);

            if(oiDB.getQuantity() < oi.getQuantity()) {
                // Seller increased the quantity
                throw new Exception();
            } else {
                // We permit the modification
                orderItemRepo.registerModified(oi);
            }
        }
    }
}
