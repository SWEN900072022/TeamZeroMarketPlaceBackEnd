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




    public void removeSeller(Seller seller) {
        sellerList.removeIf(t -> (
                seller.getUserId() == t.getUserId()
                ));
    }



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
