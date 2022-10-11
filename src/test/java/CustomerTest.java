import Domain.Customer;
import Domain.Listing;
import MockClasses.MockRepository;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import javax.money.Monetary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        Listing l = customer.bid(2, Money.of(3, Monetary.getCurrency("AUD")));

        // check the elements of l
        assertEquals(l.getListingId(), 2);
    }
}
