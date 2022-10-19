import Domain.*;
import Enums.ListingTypes;
import Enums.UserRoles;
import MockClasses.MockRepository;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import javax.money.Monetary;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SellerTest {
    private MockRepository repo;
    private Seller seller;

    public SellerTest() {
        this.repo = new MockRepository();
        this.seller = (Seller) User.create(
                "b",
                "b",
                "b",
                2,
                UserRoles.SELLER.toString()
        );
        seller.setRepo(repo);
    }

    @Test
    public void createListingCorrectly() {

       Listing listingList = seller.createListing(1, ListingTypes.FIXED_PRICE,"Jacket","Winter Jacket",1,
               Money.of(3, Monetary.getCurrency("AUD")), LocalDateTime.of(2022, 1, 1,
                       10, 10),LocalDateTime.of(2022, 2, 1,
                       10, 10));

        assertNotNull(listingList);
        assertEquals(listingList.getListingId(),0);
        assertEquals(listingList.getGroupId(),1);
        assertEquals(listingList.getTitle(),"Jacket");
    }

    @Test
    public void createListingAuction() {

        Listing listingList = seller.createListing(1, ListingTypes.AUCTION,"Jacket","Winter Jacket",1,
                Money.of(3, Monetary.getCurrency("AUD")), LocalDateTime.of(2022, 1, 1,
                        10, 10),LocalDateTime.of(2022, 2, 1,
                        10, 10));

        assertNotNull(listingList);
        assertEquals(listingList.getListingId(),0);
        assertEquals(listingList.getGroupId(),1);
        assertEquals(listingList.getTitle(),"Jacket");
    }

    @Test
    public void createListing() {
        //Test for creating Listing
        Listing listingList = Listing.create(1, 1,ListingTypes.FIXED_PRICE,"Jacket","Winter Jacket",2,
                Money.of(3, Monetary.getCurrency("AUD")), LocalDateTime.of(2022, 1, 1,
                        10, 10),LocalDateTime.of(2022, 2, 1,
                        10, 10));

        assertNotNull(listingList);
        assertEquals(listingList.getListingId(),1);
        assertEquals(listingList.getGroupId(),1);
        assertEquals(listingList.getTitle(),"Jacket");
    }

//    @Test
//    public void deleteListingCorrectly(){
//        Listing listingList = seller.deleteListing(1,1);
//
//        assertEquals(listingList.getListingId(),1);
//        assertEquals(listingList.getGroupId(),1);
//        assertNotNull(listingList);
//    }


    @Test
    public void deleteListingCorrectly(){
        Listing listingList = seller.deleteListing(1,1);

        assertEquals(listingList.getListingId(),1);
        assertEquals(listingList.getGroupId(),1);
        assertNotNull(listingList);
    }

    @Test
    public void deleteListingGroupIdError(){
        Listing listingList = seller.deleteListing(1,1);

        assertNotNull(listingList);
    }

    @Test
    public void deleteListingListingIdError(){
        Listing listingList = seller.deleteListing(999,1);

        assertNull(listingList);
    }


    @Test
    public void viewSellerListingsCorrectly(){
        List<Listing> listingList = seller.viewSellerListings(1);

        assertNotNull(listingList);
    }

//    @Test
//    public void viewSellerListingsError(){
//        List<Listing> listingList = seller.viewSellerListings(0);
//
//        //assertNull(listingList);
//        assertNotEquals(listingList.,);
//    }

    @Test
    public void viewSellerListings(){
        List<Listing> listingList = seller.viewSellerListings(1);

        assertNotNull(listingList);
    }

    @Test
    public void modifyOrderCorrectly(){
        List<EntityObject> listingList = seller.modifyOrder(1,1,1,2);


        assertNotNull(listingList);

    }


    @Test
    public void viewOrdersCorrectly() {
        List<OrderItem> ordersList = seller.viewOrders();

        assertNotNull(ordersList);
    }

    @Test
    public void  viewOrdersGetByUserId() {
        List<Order> orderList= Order.getOrdersByUserId(1,repo);

        assertNotNull(orderList);
    }

    @Test
    public void viewOrders() {
        //Test for OrderItem
        OrderItem orderItemList=OrderItem.create(1,1,2,Money.of(3, Monetary.getCurrency("AUD")));
        List<OrderItem> oiList = new ArrayList<>();
        oiList.add(orderItemList);

        assertNotNull(oiList);
    }

    @Test
    public void viewFullOrderCorrectly() {
        List<Order> fullOrderList = seller.viewFullOrder(1);

        assertNotNull(fullOrderList );
    }


    @Test
    public void viewFullOrder() {
        //Test fo ，，，，r Order
        List<Order> fullOrderList = new ArrayList<Order>();
        Order order = Order.create(1,1,"Melbourne");
        fullOrderList.add(order);
        assertNotNull(fullOrderList );
        assertEquals(order.getOrderId(),1);
        assertEquals(order.getUserId(),1);
        assertEquals(order.getAddress(),"Melbourne");

    }

    @Test
    public void cancelOrderCorrectly(){

        Order ordersList = seller.cancelOrder(1,1);
        assertEquals(ordersList.getOrderId(),1);
        assertNotNull(ordersList);
    }

    @Test
    public void cancelOrderOrderIdError(){

        Order orderList = seller.cancelOrder(999,1);

        assertNull(orderList);
    }

}
