import Domain.*;
import Enums.ListingTypes;
import MockClasses.MockRepository;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import javax.money.Monetary;
import java.util.ArrayList;
import java.util.List;
import org.junit.*;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerTest {
    private MockRepository repo;
    private Customer customer;

    public CustomerTest() {
        this.repo = new MockRepository();
        this.customer = new Customer("a", "a", "a", 1);
        customer.setRepo(repo);
    }

    @Test
    public void BidCorrectly() {
        Bid l = customer.bid(2, Money.of(3, Monetary.getCurrency("AUD")));

        // check the elements of l
        assertEquals(l.getListingId(), 2);
    }

    @Test
    public void BidOnFixedPrice() {
        Bid l = customer.bid(1, Money.of(3, Monetary.getCurrency("AUD")));

        assertNull(l);
    }

    @Test
    public void OutOfIdBid() {
        Bid l = customer.bid(300, Money.of(3, Monetary.getCurrency("AUD")));

        assertNull(l);
    }

    @Test
    public void bidTooSmall() {
        Bid l = customer.bid(2, Money.of(1, Monetary.getCurrency("AUD")));

        assertNull(l);
    }

    @Test
    public void  viewAllOrdersCorrectly() {

        List<Order> orderList = customer.viewAllOrders();
        Order.getOrdersByUserId(1,repo);
        assertNotNull(orderList);
    }

    @Test
    public void  viewAllOrders() {
        // Test for orders in the view all orders
        Order order= Order.create(1,1,"Melbourne");
        assertEquals(order.getOrderId(),1);
        assertEquals(order.getUserId(),1);
        assertEquals(order.getAddress(),"Melbourne");
    }

    @Test
    public void  viewAllOrdersGetByUserId() {
        // test get orders by user id in view all orders
        List<Order> orderList= Order.getOrdersByUserId(1,repo);
        assertNotNull(orderList);
    }

    @Test
    public void  viewAllOrdersItems() {
        //Test for view all orders
        OrderItem orderItemList=OrderItem.create(1,1,2,Money.of(3, Monetary.getCurrency("AUD")));
        List<OrderItem> oiList = new ArrayList<>();
        oiList.add(orderItemList);
        Order order= Order.create(1,1,"Melbourne",oiList);

        assertNotNull(order);
        assertEquals(order.getOrderItemList(),oiList);
        assertEquals(order.getOrderId(),1);
        assertEquals(order.getUserId(),1);
        assertEquals(order.getAddress(),"Melbourne");
    }

    @Test
    public void cancelOrderCorrectly(){
        Order orderList = customer.cancelOrder(1);

        assertEquals(orderList.getOrderId(),1);
        assertNotNull(orderList);
    }

    @Test
    public void cancelNullOrder(){
        Order orderList = customer.cancelOrder(999);

        assertNull(orderList);
    }


    @Test
    public void modifyOrdersCorrectly(){
        List<EntityObject> orderList = customer.modifyOrder(1,1,3);

        assertNotNull(orderList);

    }


    @Test
    public void checkoutListingItem(){
        // test when one item in the listing
        OrderItem item = OrderItem.create(1,1,2,Money.of(3, Monetary.getCurrency("AUD")));
        List<OrderItem> oiList = new ArrayList<>();
        oiList.add(item);
        Order orderList = customer.checkoutListing("Melbourne", oiList);
        assertEquals(orderList.getAddress(),"Melbourne");
        assertNotNull(oiList);
}

    @Test
    public void checkoutListingItems(){
        // test when more than one item in the listing
        OrderItem item1 = OrderItem.create(1,1,2,Money.of(3, Monetary.getCurrency("AUD")));
        OrderItem item2 = OrderItem.create(1,2,3,Money.of(4, Monetary.getCurrency("AUD")));
        List<OrderItem> oiList = new ArrayList<>();
        oiList.add(item1);
        oiList.add(item2);
        Order orderList = customer.checkoutListing("Melbourne", oiList);
        assertEquals(orderList.getAddress(),"Melbourne");
        assertNotNull(oiList);
    }


}
