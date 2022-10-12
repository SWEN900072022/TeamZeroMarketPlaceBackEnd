package Domain;

import Enums.ListingTypes;
import Enums.UserRoles;
import UnitofWork.IUnitofWork;
import org.javamoney.moneta.Money;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
    private UserRoles userRoles = UserRoles.CUSTOMER;
    private List<Order> orderList;
    private List<Listing> listingList;
    private IUnitofWork repo;

    public Customer(String email, String username, String password, int userId) {
        super(email, username, password, userId);
    }

    @Override
    public String getRole() {
        return UserRoles.CUSTOMER.toString();
    }

    public void setRepo(IUnitofWork repo) {
        this.repo = repo;
    }

    public Order checkoutListing(String address, List<OrderItem> oiList) {
        // Create an order object
        // Generate order id
        Order ord = Order.getLastOrderItem(repo);
        int orderId;
        if(ord == null) {
            orderId = 1;
        } else {
            orderId = ord.getOrderId();
        }
        return Order.create(orderId, getUserId(), address, oiList);
    }

    public Bid bid(int listingId, Money bidAmount) {
        Listing l = Listing.getListingById(listingId, repo);

        if(l == null) {
            return null;
        }

        // Check if it is an auction listing, if yes cast it
        if(l.getType() == ListingTypes.AUCTION) {
            AuctionListing al = (AuctionListing) l;
            Bid bid = al.bid(bidAmount, LocalDateTime.now(), getUserId());
            return bid;
        }
        return null;
    }

    public List<Order> viewAllOrders() {
        // Check to see if the order list is empty
        // Lazy load if yes
        if(orderList == null) {
            orderList = Order.getOrdersByUserId(getUserId(), repo);
        }

        return orderList;
    }

    public List<EntityObject> modifyOrder(int orderId, int listingId, int quantity) {
        // Given some order, change the order object
        // Need to check the stock level as well
        // Check to see if the order is in the orderList,if not load from
        // db
        if(orderList == null) {
            orderList = Order.getOrdersByUserId(getUserId(), repo);
        }

        Listing l = Listing.getListingById(listingId, repo);

        if(l != null && orderList != null) {
            for(Order ord : orderList) {
                // Check the stock level
                if(ord.getOrderId() == orderId) {
                    int initialQuantity = ord.getOrderItem(listingId).getQuantity();
                    OrderItem oi = ord.modifyOrderItem(listingId, quantity, l.getQuantity());
                    l.setQuantity(l.getQuantity() + (initialQuantity - oi.getQuantity()));

                    List<EntityObject> result = new ArrayList<>();
                    result.add(ord);
                    result.add(l);

                    return result;
                }
            }
        }

        throw new IllegalArgumentException();
    }

    public Order cancelOrder(int orderId) {
        if(orderList == null) {
            orderList = Order.getOrdersByUserId(getUserId(), repo);
        }

        for(int i = 0; i < orderList.size(); i++) {
            Order ord = orderList.get(i);
            if(ord.getOrderId() == orderId) {
                orderList.remove(i);
                return ord;
            }
        }
        return null;
    }
}
