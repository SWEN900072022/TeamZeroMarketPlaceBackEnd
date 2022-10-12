package Domain;

import Enums.ListingTypes;
import Enums.UserRoles;
import Injector.FindConditionInjector.FindIdInjector;
import Injector.FindConditionInjector.FindOrderForSellerGroupInjector;
import Service.Counter;
import UnitofWork.IUnitofWork;
import UnitofWork.IUnitofWork;
import Util.GeneralUtil;
import org.javamoney.moneta.Money;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Seller extends User{
    private UserRoles userRoles = UserRoles.SELLER;
    private SellerGroup sg;
    private IUnitofWork repo;
    private List<Listing> listingList;
    private List<OrderItem> ordersList;
    private List<Order> fullOrderList;

    private int groupId;

    public void setRepo(IUnitofWork repo){
        this.repo = repo;
    }

    public void setSellerGroup(SellerGroup group){
        this.sg = group;
    }

    public Seller(String email, String username, String password, int userId) {
        super(email, username, password, userId);
    }

    public void setGroupId(int groupId){
        this.groupId=groupId;
    }

    @Override
    public String getRole() {
        return UserRoles.SELLER.toString();
    }

    public Listing createListing(ListingTypes type, String title, String description, int quantity, Money price, LocalDateTime startTime, LocalDateTime endTime) {
        if(sg!=null) {
            int id = Counter.increment(Listing.class);
            Listing l = Listing.create(id, groupId, type, title, description, quantity, price, startTime, endTime);
            listingList.add(l);
            return l;
        }
        throw new IllegalArgumentException();
    }

    public Listing deleteListing(int listingId) {
        if(listingList==null){
            viewSellerListings();
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


    public List<Listing> viewSellerListings() {
        // Check to see if the order list is empty
        // Lazy load if yes
        Filter filter = new Filter("groupId", this.groupId);
        List<Filter> filterCondition = new ArrayList<>();
        filterCondition.add(filter);
        if(listingList == null) {
            listingList = Listing.getListingByFilterCondition(filterCondition, repo);
        }
        return listingList;
    }

    public List<OrderItem> viewOrders() {
        // Check to see if the order list is empty
        // Lazy load if yes
        if(ordersList == null) {
            ordersList = Order.getOrdersByGroupId(getUserId(), repo);
        }

        return ordersList;
    }

    public List<Order> viewFullOrder() {
        // Check to see if the order list is empty
        // Lazy load if yes
        List<Integer> tempList = new ArrayList<Integer>();
        List<Order> fullOrderList = new ArrayList<Order>();
        if(ordersList == null) {
            ordersList = Order.getOrdersByGroupId(getUserId(), repo);
        }
        for(OrderItem item: ordersList){
            tempList.add(item.getOrderId());
        }
        Set<Integer> set = new HashSet<Integer>(tempList);
        for(int i: set){
            Order temp = Order.create(i, getUserId(), "address");
            fullOrderList.add(temp);
        }

        return fullOrderList;
    }

//    public List<Order> getOrders(){
//
//    }

    public OrderItem modifyOrder(int orderId, int listingId, int quantity) {
        // Lazy load if orderItemList is empty
        if(ordersList == null) {
            ordersList = Order.getOrdersByGroupId(getUserId(), repo);
        }
        for(OrderItem item : ordersList) {
            if(item.getListingId() == listingId) {
                item.setQuantity(quantity);
                return item;
            }
        }
        return null;
    }

    public Order modifyOrder1(int orderId, int listingId, int quantity) {
        // Given some order, change the order object
        // Need to check the stock level as well
        // Check to see if the order is in the orderList,if not load from
        // db
        if(fullOrderList == null) {
           fullOrderList = viewFullOrder();
        }

        Listing l = Listing.getListingById(listingId, repo);

        if(l != null && fullOrderList != null) {
            for(Order ord : fullOrderList) {
                if(ord.getOrderId() == orderId) {
                    ord.modifyOrderItem(listingId, quantity);
                    return ord;
                }
            }
        }

        throw new IllegalArgumentException();
    }

//    public List<OrderItem> cancelOrder(int orderId) {
//        List<OrderItem> result = new ArrayList<OrderItem>();
//
//        if(ordersList == null) {
//            ordersList = Order.getOrdersByGroupId(getUserId(), repo);
//        }
//        for(int i = 0; i < ordersList.size(); i++) {
//            OrderItem ord = ordersList.get(i);
//            if(ord.getOrderId() == orderId) {
//                ordersList.remove(i);
//                result.add(ord);
//            }
//        }
//        return result;
//    }
    public Order cancelOrder(int orderId) {
        if(fullOrderList == null) {
            fullOrderList = viewFullOrder();
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
