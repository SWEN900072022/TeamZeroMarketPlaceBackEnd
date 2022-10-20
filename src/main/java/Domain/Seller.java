package Domain;

import Enums.ListingTypes;
import Enums.UserRoles;
import PessimisticLock.LockManager;
import UnitofWork.IUnitofWork;
import org.javamoney.moneta.Money;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Seller extends User{
    private UserRoles role = UserRoles.SELLER;
    private SellerGroup sg;
    private List<Listing> listingList;
    private List<OrderItem> ordersList;
    private List<Order> fullOrderList;


    public Seller(String email, String username, String password, int userId) {
        super(email, username, password, userId);
    }

    @Override
    public String getRole() {
        return UserRoles.SELLER.toString();
    }

    public Listing createListing(int groupId, ListingTypes type, String title, String description, int quantity, Money price, LocalDateTime startTime, LocalDateTime endTime) {
        if(listingList==null){
            viewSellerListings(groupId);
        }

        Listing l = Listing.create(0, groupId, type, title, description, quantity, price, startTime, endTime);
        listingList.add(l);
        return l;
    }

    public Listing deleteListing(int listingId, int groupId) {
        if(listingList==null){
            viewSellerListings(groupId);
        }

        if(groupId!=0){
            for(int i = 0; i < listingList.size(); i++){
                Listing item = listingList.get(i);
                if(item.getListingId()==listingId){
                    listingList.remove(i);
                    return item;
                }
            }
        }
        return null;
    }


    public List<Listing> viewSellerListings(int groupId) {
        // Check to see if the order list is empty
        // Lazy load if yes
        Filter filter = new Filter("groupId", groupId);
        List<Filter> filterCondition = new ArrayList<>();
        filterCondition.add(filter);
        if(listingList == null) {
            listingList = Listing.getListingByFilterCondition(filterCondition, getRepo());
        }
        return listingList;
    }

    public List<OrderItem> viewOrders() {
        // Check to see if the order list is empty
        // Lazy load if yes
        if(ordersList == null) {
            ordersList = Order.getOrdersByGroupId(getUserId(), getRepo());
        }

        return ordersList;
    }

    public List<Order> viewFullOrder(int groupId) {
        // Check to see if the order list is empty
        // Lazy load if yes
        List<Integer> tempList = new ArrayList<Integer>();
        List<Order> fullOrderList = new ArrayList<Order>();
        if(ordersList == null) {
            ordersList = Order.getOrdersByGroupId(groupId, getRepo());
        }
        for(OrderItem item: ordersList){
            tempList.add(item.getOrderId());
        }
        Set<Integer> set = new HashSet<Integer>(tempList);
        for(int i: set){
            Order temp = Order.create(i, getUserId(), "address", ordersList);
            fullOrderList.add(temp);
        }

        return fullOrderList;
    }

    public List<EntityObject> modifyOrder(int orderId, int listingId, int groupId, int quantity) {
        // Given some order, change the order object
        // Need to check the stock level as well
        // Check to see if the order is in the orderList,if not load from
        // db
        if(fullOrderList == null) {
            // The list of orders owned by the seller
           fullOrderList = viewFullOrder(groupId);
        }

        Listing l = Listing.getListingById(listingId, getRepo());

        if(l != null && fullOrderList != null) {
            for(Order ord : fullOrderList) {
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

    public Order cancelOrder(int orderId, int groupId) {
        if(fullOrderList == null) {
            fullOrderList = viewFullOrder(groupId);
        }
        for(int i = 0; i < fullOrderList.size(); i++) {
            Order ord = fullOrderList.get(i);
            if(ord.getOrderId() == orderId) {
                ordersList.remove(i);
                return ord;
            }
        }
        return null;
    }
}
