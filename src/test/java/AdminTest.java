import Domain.*;
import Enums.ListingTypes;
import Enums.UserRoles;
import MockClasses.MockRepository;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.money.Monetary;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AdminTest {
    private MockRepository repo;
    private Admin admin;

    public AdminTest() {
        this.repo = new MockRepository();
        this.admin = (Admin) User.create("c", "c", "c", 3,
                UserRoles.ADMIN.toString()
        );
        admin.setRepo(repo);
    }

    @BeforeEach
    public void create(){
        SellerGroup sg= admin.createSellerGroup("Team0");

    }

    @Test
    public void getAllPurchasesCorrectly(){
        List<OrderItem> ordersList= admin.getAllPurchases();

        assertNotNull(ordersList);
    }

    @Test
    public void getAllPurchasesRetrieve(){
        List<Order> orders = Order.getAllOrders(repo);

        assertNotNull(orders);
    }

    @Test
    public void getAllPurchasesItems(){

        //Test for all orders get the order items and return the list of order items
        OrderItem orderitem=OrderItem.create(1,1,2,Money.of(3, Monetary.getCurrency("AUD")));
        List<OrderItem> oiList = new ArrayList<>();
        oiList.add(orderitem);
        Order ord = Order.create(1,1,"Melbourne",oiList);
        ord.setRepository(repo);
        List<OrderItem> orderItemList = ord.getOrderItemList();
        assertEquals(orderItemList,oiList);
        assertNotNull(orderItemList);
    }

    @Test
    public void getAllPurchasesItem(){

        //Test for all orders get the order items and return the list of order items
        Order ord = Order.create(1,1,"Melbourne");
        ord.setRepository(repo);
        List<OrderItem> orderItemList = ord.getOrderItemList();
        assertNotNull(orderItemList);
    }

    @Test
    public void getAllSellerGroupCorrectly(){
        List<SellerGroup> sellerGroups= admin.getAllSellerGroup(repo);
        assertNotNull(sellerGroups);
    }

    @Test
    public void getAllSellerGroup(){
        //Test get All Seller Group
        List<SellerGroup> sellerGroups = SellerGroup.getAllSellerGroup(repo);
        assertNotNull(sellerGroups);
    }

//    @Test
//    public void getAllSellerGroupSellers(){
//
//        List<SellerGroup> sellerGroups = SellerGroup.getAllSellerGroup(repo);
//        assertNotNull(sellerGroups);
//    }

    @Test
    public void getAllUsersCorrectly(){
        List<User> users= admin.getAllUsers();

        assertNotNull(users);
    }

    @Test
    public void getAllUsers(){
        List<User> users = User.getAllUser(repo);

        assertNotNull(users);
    }

    @Test
    public void getAllUsersUser(){
        //Test one user
        User user1 = User.create("123@123.com", "a", "a", 1, "customer");
        List<User> usersList = new ArrayList<>();
        usersList.add(user1);

        assertNotNull(usersList);
        assertEquals(user1.getUsername(),"a");
        assertEquals(user1.getEmail(),"123@123.com");
        assertEquals(user1.getPassword(),"a");
        assertEquals(user1.getUserId(),1);
        assertEquals(user1.getRole(),"customer");
    }

//    @Test
//    public void getAllUsersRoleError(){
//        //Test one user
//        User user1 = User.create("123@123.com", "a", "a", 1, "Customer");
//        List<User> usersList = new ArrayList<>();
//        usersList.add(user1);
//
//        assertNotNull(usersList);
//    }

    @Test
    public void getAllUsersUsers(){
        //Test more than one user
        User user1 = User.create("123@123.com", "a", "a", 1, "customer");
        User user2 = User.create("1234@1234.com", "b", "b", 2, "seller");
        List<User> usersList = new ArrayList<>();
        usersList.add(user1);
        usersList.add(user2);

        assertNotNull(usersList);
        assertEquals(user1.getUsername(),"a");
        assertEquals(user1.getEmail(),"123@123.com");
        assertEquals(user1.getPassword(),"a");
        assertEquals(user1.getUserId(),1);
        assertEquals(user1.getRole(),"customer");
        assertEquals(user2.getUsername(),"b");
        assertEquals(user2.getEmail(),"1234@1234.com");
        assertEquals(user2.getPassword(),"b");
        assertEquals(user2.getUserId(),2);
        assertEquals(user2.getRole(),"seller");
    }

    @Test
    public void createSellerGroupCorrectly(){
        SellerGroup sellergroup= admin.createSellerGroup("Team0");

        assertNotNull(sellergroup);
    }

    @Test
    public void createSellerGroupError(){
        SellerGroup sellergroup= admin.createSellerGroup("a");

        assertNull(sellergroup);
    }


    @Test
    public void addSellerToGroupCorrectly(){

        GroupMembership groupmembership= admin.addSellerToGroup("a",3);

        assertNotNull(groupmembership);
    }

    @Test
    public void addSellerToGroupNull(){
        GroupMembership groupmembership= admin.addSellerToGroup("Team0",1);

        assertNull(groupmembership);
    }

//    @Test
//    public void addSellerToGroupError(){
//        GroupMembership groupmembership= admin.addSellerToGroup("Team0",9999);
//
//        assertNull(groupmembership);
//    }

    @Test
    public void removeSellerFromGroupCorrectly(){
        GroupMembership groupmembership= admin.removeSellerFromGroup("a",1);

        assertNotNull(groupmembership);
    }

    @Test
    public void removeSellerFromGroup(){
        GroupMembership groupmembership= admin.removeSellerFromGroup("Team0",1);

        assertNull(groupmembership);
    }

    @Test
    public void removeListing(){
        Listing l= admin.removeListing(1);

        assertNotNull(l);
    }

    @Test
    public void removeListingIdErr(){
        Listing l= admin.removeListing(0);

        assertNull(l);
    }

    @Test
    public void removeListingIdError(){
        Listing l= admin.removeListing(9999);

        assertNull(l);
    }


}
