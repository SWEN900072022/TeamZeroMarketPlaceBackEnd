package Domain;

import Enums.UserRoles;

import java.util.List;

public class Admin extends User{
    private UserRoles userRoles = UserRoles.ADMIN;
    private List<SellerGroup> sellerGroupList;
    private List<User> userGroupList;

    public Admin(String email, String username, String password, int userId) {
        super(email, username, password, userId);
    }

    @Override
    public String getRole() {
        return UserRoles.ADMIN.toString();
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