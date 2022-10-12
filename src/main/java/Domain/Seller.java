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
import java.util.List;

public class Seller extends User{
    private UserRoles userRoles = UserRoles.SELLER;
    private SellerGroup sg;
    private IUnitofWork repo;
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

    public int getGroupId(){
        List<Object> param = new ArrayList<>();
        param.add(getUserId());
        GroupMembership result = (GroupMembership) repo.read(new FindIdInjector("groupmembership"), param,
                GroupMembership.class);
        this.groupId = result.getGroupId();
    }

    @Override
    public String getRole() {
        return UserRoles.SELLER.toString();
    }

    public Listing createListing(ListingTypes type, String title, String description, int quantity, Money price, LocalDateTime startTime, LocalDateTime endTime) {
        if(sg!=null) {
            int id = Counter.increment(Listing.class);
            return sg.addListing(id, type, title, description, quantity, price, startTime, endTime);
        }
        throw new IllegalArgumentException();
    }

    public List<OrderItem> viewOrders() {
        if (sg==null){
           return null;
        }
        return sg.viewAllOrders(getUserId());
    }


//    public void modifyListing(Listing l) {
//        sg.modifyListing(l);
//    }

//    public boolean decreaseQuantity(OrderItem oi, int quantity) {
//
//        if(oi.getQuantity() < quantity ) {
//            // Too big, fail
//            return false;
//        }
//        // Not too bid
////        sg.modifyOrderItem(oi);
//        return true;
//    }

    public void modifyOrder(int orderId, int listingId, int quantity){
        if (sg!=null){
            sg.modifyOrder(orderId, listingId,getUserId(), quantity);
        }
        throw new IllegalArgumentException();
    }

    public List<OrderItem> cancelOrder(int orderId) {
        if(sg!=null){
            return sg.deleteOrderItem(orderId, getUserId());
        }
        return null;
    }
}
