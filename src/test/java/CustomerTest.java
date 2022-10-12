import Domain.Bid;
import Domain.Customer;
import Domain.Listing;
import Domain.Order;
import Enums.ListingTypes;
import MockClasses.MockRepository;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import javax.money.Monetary;

import java.util.List;

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
    public void getOrdersCorrectly() {
        List<Order> orderList = customer.viewAllOrders();

        assertNull(orderList);
    }
}
