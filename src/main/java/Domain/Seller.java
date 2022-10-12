package Domain;

import Enums.UserRoles;

public class Seller extends User{
    private UserRoles userRoles = UserRoles.SELLER;
    private SellerGroup sg;

    public Seller(String email, String username, String password, int userId) {
        super(email, username, password, userId);
    }

    @Override
    public String getRole() {
        return UserRoles.SELLER.toString();
    }

    public void createListing(Listing l) {
        sg.addListing(l);
    }

    public void viewOrders() {

    }

    public void modifyListing(Listing l) {
        sg.modifyListing(l);
    }

    public boolean decreaseQuantity(OrderItem oi, int quantity) {
        if(oi.getQuantity() < quantity ) {
            // Too big, fail
            return false;
        }
        // Not too bid
        sg.modifyOrderItem(oi);
        return true;
    }

    public void cancelOrder(OrderItem oi) {
        sg.deleteOrderItem(oi);
    }
}
