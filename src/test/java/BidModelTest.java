import Entity.Bid;
import MockClasses.MockRepository;
import Model.BidModel;
import Util.JWTUtil;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import javax.money.Monetary;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BidModelTest {
    private MockRepository repo;
    private BidModel bidModel;
    private String jwt;

    public BidModelTest() {
        this.repo = new MockRepository();
        this.bidModel = new BidModel(repo);
        jwt = JWTUtil.generateToken("1", new HashMap<>());
    }

    @Test
    public void CreateBidInvalidJWTToken() {
        boolean isSuccessful = bidModel.createBid(new Bid(), "");
        assertFalse(isSuccessful);
    }

    @Test
    public void CreateBidLateBid() {
        Bid bid = new Bid();
        bid.setUserId(1); // Listing one is a bid that ends at LocalDateTime.now()
        bid.setListingId(1);
        boolean isSuccessful = bidModel.createBid(bid, jwt);
        assertFalse(isSuccessful);
    }

    @Test
    public void CreateBidBidLesserThanHighest() {
        Bid bid = new Bid();
        bid.setUserId(1);
        bid.setListingId(2);
        bid.setBidAmount(Money.of(5.00, Monetary.getCurrency("AUD")));
        boolean isSuccessful = bidModel.createBid(bid, jwt);
        assertFalse(isSuccessful);
    }

    @Test
    public void CreateBidValidBid() {
        Bid bid = new Bid();
        bid.setUserId(1);
        bid.setListingId(2);
        bid.setBidAmount(Money.of(20.00, Monetary.getCurrency("AUD")));
        boolean isSuccessful = bidModel.createBid(bid, jwt);
        assertTrue(isSuccessful);
    }

    @Test
    public void CreateBidNullBid() {
        boolean isSuccessful = bidModel.createBid(null, jwt);
        assertFalse(isSuccessful);
    }
}
