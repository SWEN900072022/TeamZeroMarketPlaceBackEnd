package Domain;

import Enums.UserRoles;

import java.util.List;

public class Admin extends User{
    private UserRoles userRoles = UserRoles.ADMIN;
    private List<SellerGroup> sellerGroupList;
    private List<User> userGroupList;

    public Admin(String email, String username, String password, int userId, UserRoles userRoles, List<Order> orderList, List<Listing> listingList) {
        super(email, username, password, userId);
        this.userRoles = userRoles;
    }

    public void onboardSeller() {
        // Get the listings from the seller groups
    }

    public void viewAllPurchases() {

    }

    public void createSellerGroup() {

    }

    public void addSellerToGroup() {

    }

    public void removeSellerFromGroup() {

    }

    public void removeListing() {

    }
}
