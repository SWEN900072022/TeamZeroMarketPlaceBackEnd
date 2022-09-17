package Model;

import Entity.Listing;
import Entity.Order;
import Entity.OrderItem;
import Enums.ListingTypes;
import Injector.FindIdInjector;
import Mapper.ListingMapper;
import Mapper.OrderItemMapper;
import Mapper.OrderMapper;
import UnitofWork.IUnitofWork;
import UnitofWork.Repository;
import Util.JWTUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderModel {
    private IUnitofWork<Order> orderRepo;
    private IUnitofWork<Listing> listingRepo;
    private IUnitofWork<OrderItem> orderItemRepo;

    public OrderModel() {
        orderRepo = new Repository<Order>(new OrderMapper());
        listingRepo = new Repository<Listing>(new ListingMapper());
        orderItemRepo = new Repository<OrderItem>(new OrderItemMapper());
    }

    public OrderModel(IUnitofWork<Order> orderRepo, IUnitofWork<Listing> listingRepo, IUnitofWork<OrderItem> orderItemRepo) {
        this.orderRepo = orderRepo;
        this.listingRepo = listingRepo;
        this.orderItemRepo = orderItemRepo;
    }

    public boolean createOrderItem(List<OrderItem> orderItemList,Order order, String jwt) {
        // Check to see if the jwt token is valid
        try {
            if(!JWTUtil.validateToken(jwt)) {
                return false;
            } else {
                // Set the user id for the order
                order.setUserId(Integer.parseInt(JWTUtil.getSubject(jwt)));
            }
        } catch (Exception e) {
            return false;
        }

        // First we validate that the quantity is sufficient
        for (OrderItem oi : orderItemList) {
            List<Object> param = new ArrayList<>();
            param.add(oi.getListingId());
            Listing l = listingRepo.read(new FindIdInjector("listing"), param, Integer.toString(oi.getListingId()));
            if(l == null) {
                // The listing does not exist
                return false;
            }

            if(l.getQuantity() < oi.getQuantity()) {
                // There isn't enough to support the order
                return false;
            } else {
                l.setQuantity(l.getQuantity() - oi.getQuantity());
            }

            listingRepo.registerModified(l);
            orderItemRepo.registerNew(oi);
        }

        // Push changes
        try {
            orderRepo.registerNew(order);
            orderRepo.commit();
            listingRepo.commit();
            orderItemRepo.commit();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
