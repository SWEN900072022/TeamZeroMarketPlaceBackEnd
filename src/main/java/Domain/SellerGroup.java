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
    private List<OrderItem> ordersList;
    private IUnitofWork repo;

    public void setRepo(IUnitofWork repo){
        this.repo = repo;
    }


    protected SellerGroup(int groupId, String groupName, List<Seller> sellerList, List<Listing> listingList, List<OrderItem> ordersList) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.sellerList = sellerList;
        this.listingList = listingList;
        this.ordersList = ordersList;
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

    public static SellerGroup create(int groupId, String groupName) {
        return new SellerGroup(groupId, groupName, null, null, null);
    }

    public static SellerGroup create(int groupId, String groupName, List<Seller>sellerList, List<Listing> listingList, List<OrderItem> ordersList) {
        return new SellerGroup(groupId, groupName, sellerList, listingList, ordersList);
    }

    public List<OrderItem> viewAllOrders(int userId) {
        // Check to see if the order list is empty
        // Lazy load if yes
        if(ordersList == null) {
            ordersList = Order.getOrdersByGroupId(userId, repo);
        }

        return ordersList;
    }

    public List<Listing> viewSellerListings() {
        // Check to see if the order list is empty
        // Lazy load if yes
        Filter filter = new Filter("groupId",getGroupId());
        List<Filter> filterCondition = new ArrayList<>();
        filterCondition.add(filter);
        if(listingList == null) {
            listingList = Listing.getListingByFilterCondition(filterCondition, repo);
        }

        return listingList;
    }


    public void removeSeller(Seller seller) {
        sellerList.removeIf(t -> (
                seller.getUserId() == t.getUserId()
                ));
    }

    public Listing addListing(int listingId, ListingTypes type, String title, String description, int quantity, Money price, LocalDateTime startTime, LocalDateTime endTime) {
//        if(listing.getGroupId() != groupId) {
//            return false;
//        }
        Listing l = Listing.create(listingId, getGroupId(), type, title, description, quantity, price, startTime, endTime);
        listingList.add(l);
        return l;
    }

//    public void modifyListing(Listing listing) {
//        deleteListing(listing);
//        addListing(listing);
//    }

    public void deleteListing(int listingId) {
        if(listingList==null){
            listingList = viewSellerListings();
        }
        for(int i = 0; i < ordersList.size(); i++){
            Listing item = listingList.get(i);
            if(item.getListingId()==listingId){
                listingList.remove(i);
            }
        }
    }

//    public void addOrderItem(OrderItem oi) {
//        ordersList.add(oi);
//    }

    public OrderItem modifyOrder(int orderId, int listingId,int userId, int quantity) {
        // Lazy load if orderItemList is empty
        if(ordersList == null) {
            ordersList = Order.getOrdersByGroupId(userId, repo);
        }

        for(OrderItem item : ordersList) {
            if(item.getListingId() == listingId) {
                item.setQuantity(quantity);
                return item;
            }
        }
        return null;
    }

    public List<OrderItem> deleteOrderItem(int orderId, int userId) {
        List<OrderItem> result = new ArrayList<OrderItem>();

        if(ordersList == null) {
            ordersList = Order.getOrdersByGroupId(userId, repo);
        }
        for(int i = 0; i < ordersList.size(); i++) {
            OrderItem ord = ordersList.get(i);
            if(ord.getOrderId() == orderId) {
                ordersList.remove(i);
                result.add(ord);
            }
        }
        return result;
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
