package Domain;

import Injector.FindConditionInjector.FindGroupIdByNameInjector;
import UnitofWork.IUnitofWork;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import Enums.ListingTypes;
import java.time.LocalDateTime;
import org.javamoney.moneta.Money;


public class SellerGroup {
    private int groupId;
    private String groupName;
    private List<Seller> sellerList;
    private List<Listing> listingList;
    private List<Order> ordersList;
    private IUnitofWork repo;

    public void setRepo(IUnitofWork repo){
        this.repo = repo;
    }

    protected SellerGroup(int groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
//        this.sellerList = sellerList;
//        this.listingList = listingList;
//        this.ordersList = ordersList;
    }

    public static SellerGroup create(int groupId, String groupName) {
//        List<Object> param = new ArrayList<>();
//        param.add(sg.getGroupName());
//
//        Entity.SellerGroup sgTemp = (Entity.SellerGroup) repo.read(
//                new FindGroupIdByNameInjector(),
//                param,
//                Entity.SellerGroup.class);
//
////        // At this point, we should be an admin, write to db
////        if(sgTemp.isEmpty()) {
////            repo.registerNew(sg);
////            return true;
////        }
//        return false;
//        return new SellerGroup(groupId, groupName);
    }



    public boolean addSeller(Seller seller) {
        // Check to see if the object is valid
        if(seller == null) {
            return false;
        }
        List<Object> param = new ArrayList<>();
        param.add(seller.getUserId());
        GroupMembership membership = new GroupMembership(this.groupId,seller.getUserId());
        sellerList.add(seller);
        return false;
    }

    public List<Order> viewAllOrders() {
        // Check to see if the order list is empty
        // Lazy load if yes
        if(ordersList == null) {
            ordersList = Order.getOrdersByGroupId(getGroupId(), repo);
        }

        return ordersList;
    }

    public void removeSeller(Seller seller) {
        sellerList.removeIf(t -> (
                seller.getUserId() == t.getUserId()
                ));
    }

    public Listing addListing(Class<?> clazz, int listingId, ListingTypes type, String title,String description, int quantity, Money price,Listing listing, LocalDateTime
            startTime, LocalDateTime endTime) {
//        if(listing.getGroupId() != groupId) {
//            return false;
//        }
        Listing l = Listing.create(clazz, listingId, getGroupId(), type, title, description, quantity, price, startTime, endTime);
        listingList.add(l);
        return l;
    }

//    public void modifyListing(Listing listing) {
//        deleteListing(listing);
//        addListing(listing);
//    }

    public void deleteListing() {
        listingList.removeIf(t -> (
                listing.getListingId() == t.getListingId()
        ));
    }

//    public void addOrderItem(OrderItem oi) {
//        ordersList.add(oi);
//    }

    public Order modifyOrder(int orderId, int listingId, int quantity) {
        // Lazy load if orderItemList is empty
        if(ordersList == null) {
            ordersList = Order.getOrdersByGroupId(getGroupId(), repo);
        }

        for(Order ord : ordersList) {
            if(ord.getOrderId() == orderId) {
                ord.modifyOrderItem(listingId, quantity);
                return ord;
            }
        }
        throw new IllegalArgumentException();
    }

    public Order deleteOrderItem(int orderId) {
        if(ordersList == null) {
            ordersList = Order.getOrdersByGroupId(getGroupId(), repo);
        }
        for(int i = 0; i < ordersList.size(); i++) {
            Order ord = ordersList.get(i);
            if(ord.getOrderId() == orderId) {
                ordersList.remove(i);
                return ord;
            }
        }
        throw new IllegalArgumentException();
    }

//    public List<OrderItem> getOrdersList(){
//        return ordersList;
//    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<Seller> getSellerList() {
        return sellerList;
    }

    public void setSellerList(List<Seller> sellerList) {
        this.sellerList = sellerList;
    }

    public List<Listing> getListingList() {
        return listingList;
    }

    public void setListingList(List<Listing> listingList) {
        this.listingList = listingList;
    }
}
