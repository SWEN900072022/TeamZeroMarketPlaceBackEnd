package Domain;

import Enums.UserRoles;
import UnitofWork.IUnitofWork;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Admin extends User{
    private UserRoles userRoles = UserRoles.ADMIN;
    private List<SellerGroup> sellerGroupList;
    private List<User> userGroupList;
    private IUnitofWork repo;

    public Admin(String email, String username, String password, int userId) {
        super(email, username, password, userId);
    }

    public void setRepo(IUnitofWork repo) {
        this.repo = repo;
    }

    @Override
    public String getRole() {
        return UserRoles.ADMIN.toString();
    }

    public List<OrderItem> getAllPurchases() {
        // To view all purchases, we want to retrieve it from the orders
        List<Order> orders = Order.getAllOrders(repo);

        List<OrderItem> result = new ArrayList<>();

        // For all orders get the order items and return the list of order items
        for(Order ord : orders) {
            List<OrderItem> orderItemList = ord.getOrderItemList();
            result.addAll(orderItemList);
        }
        return result;
    }

    public List<SellerGroup> getAllSellerGroup(IUnitofWork repo) {
        // Get all seller groups
        List<SellerGroup> sellerGroups = SellerGroup.getAllSellerGroup(repo);
        return sellerGroups;
    }

    public List<User> getAllUsers() {
        // Get all users
        List<User> users = User.getAllUser(repo);
        userGroupList = users;
        return users;
    }

    public SellerGroup createSellerGroup(String groupName) {
        if(sellerGroupList == null) {
            sellerGroupList = getAllSellerGroup(repo);
        }

        // Check to see if the seller group already exist
        for(SellerGroup sg : sellerGroupList) {
            if(Objects.equals(sg.getGroupName(), groupName)) {
                return null;
            }
        }

        // No existing group with the name yet
        return SellerGroup.create(0, groupName);
    }

    public GroupMembership addSellerToGroup(String groupName, int userId) {
        // With userid and groupid, we can create a seller group with the user
        SellerGroup sg = SellerGroup.getSellerGroupByGroupName(groupName, repo);

        if(sg != null) {
            // group exists add user to group
            GroupMembership gm = sg.addSeller(userId, sg.getGroupId());
            return gm;
        }
        return null;
    }

    public GroupMembership removeSellerFromGroup(String groupName, int userId) {
        SellerGroup sg = SellerGroup.getSellerGroupByGroupName(groupName, repo);

        if(sg != null) {
            // Group exists, remove user
            GroupMembership gm = sg.removeSeller(userId, sg.getGroupId());
            return gm;
        }
        return null;
    }

    public Listing removeListing(int listingId) {
        // Given the listing id, remove the listing from teh database
        // FInd the listing and delete it
        Listing l = Listing.getListingById(listingId, repo);
        return l;
    }
}
