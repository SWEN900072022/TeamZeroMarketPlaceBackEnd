package Domain;

import Injector.FindConditionInjector.FindAllInjector;
import Injector.FindConditionInjector.FindEmailAndPasswordInjector;
import Injector.FindConditionInjector.FindGroupIdByNameInjector;
import Injector.FindConditionInjector.FindIdInjector;
import UnitofWork.IUnitofWork;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import Enums.ListingTypes;
import java.time.LocalDateTime;

import Util.GeneralUtil;
import org.javamoney.moneta.Money;


public class SellerGroup extends EntityObject{
    private int groupId;
    private String groupName;
    private List<GroupMembership> sellerList;
    private List<Listing> listingList;
    private List<OrderItem> ordersList;
    private IUnitofWork repo;

    public void setRepo(IUnitofWork repo){
        this.repo = repo;
    }


    protected SellerGroup(int groupId, String groupName, List<GroupMembership> sellerList, List<Listing> listingList, List<OrderItem> ordersList) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.sellerList = sellerList;
        this.listingList = listingList;
        this.ordersList = ordersList;
    }

    public GroupMembership addSeller(int userId, int groupId) {
        // Check if the seller is already added
        GroupMembership gm = GroupMembership.getGroupMembershipByUserId(userId, repo);

        if(gm == null) {
            // The user does not belong to any groups, add them
            gm = GroupMembership.create(groupId, userId);
            return gm;
        }
        return null;
    }

    public GroupMembership removeSeller(int userId, int groupId) {
        return GroupMembership.create(groupId, userId);
    }

    public static SellerGroup create(int groupId, String groupName) {
        return new SellerGroup(groupId, groupName, null, null, null);
    }

    public static SellerGroup create(int groupId, String groupName, List<GroupMembership>sellerList, List<Listing> listingList, List<OrderItem> ordersList) {
        return new SellerGroup(groupId, groupName, sellerList, listingList, ordersList);
    }

    public static SellerGroup getSellerGroupByGroupName(String groupName, IUnitofWork repo) {
        List<Object> param = new ArrayList<>();
        param.add(groupName);

        return (SellerGroup) repo.read(
                new FindGroupIdByNameInjector(),
                param,
                SellerGroup.class
        );
    }

    public static List<SellerGroup> getAllSellerGroup(IUnitofWork repo) {
        List<Object> param = new ArrayList<>();

        // Get order details
        List<SellerGroup> ordList = GeneralUtil.castObjectInList(repo.readMulti(
                new FindAllInjector("sellergroups"),
                param,
                SellerGroup.class), SellerGroup.class);

        return ordList;
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

    public List<GroupMembership> getSellerList() {
        return sellerList;
    }

    public void setSellerList(List<GroupMembership> sellerList) {
        this.sellerList = sellerList;
    }

    public List<Listing> getListingList() {
        return listingList;
    }

    public void setListingList(List<Listing> listingList) {
        this.listingList = listingList;
    }
}
