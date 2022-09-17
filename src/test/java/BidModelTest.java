import Entity.Bid;
import MockClasses.MockBidRepository;
import MockClasses.MockListingRepository;
import Model.BidModel;
import Util.JWTUtil;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import javax.money.Monetary;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BidModelTest {
    private MockListingRepository listingRepository;
    private MockBidRepository bidRepository;
    private BidModel bidModel;
    private String jwt;

    public BidModelTest() {
        this.listingRepository = new MockListingRepository();
        this.bidRepository = new MockBidRepository();
        this.bidModel = new BidModel(listingRepository, bidRepository);
        jwt = JWTUtil.generateToken("1", new HashMap<>());
    }

    @Test
    public void CreateBidInvalidJWTToken() {
        boolean isSuccessful = bidModel.createBid(new Bid(), "");
        assertFalse(isSuccessful);
    }

    @Test
    public void CreateBidLateBid() {
        this.listingRepository.isBidEnd = true;
        this.listingRepository.isGroup = true;
        boolean isSuccessful = bidModel.createBid(new Bid(), jwt);
        assertFalse(isSuccessful);
    }

    @Test
    public void CreateBidBidLesserThanHighest() {
        this.listingRepository.isBidEnd = false;
        this.listingRepository.isGroup = true;
        this.bidRepository.isLowBid = false;

        Bid bid = new Bid();
        bid.setUserId(1);
        bid.setListingId(1);
        bid.setBidAmount(Money.of(5.00, Monetary.getCurrency("AUD")));
        boolean isSuccessful = bidModel.createBid(bid, jwt);
        assertFalse(isSuccessful);
    }

    @Test
    public void CreateBidValidBid() {
        this.listingRepository.isBidEnd = false;
        this.listingRepository.isGroup = true;
        this.bidRepository.isLowBid = true;

        Bid bid = new Bid();
        bid.setUserId(1);
        bid.setListingId(1);
        bid.setBidAmount(Money.of(5.00, Monetary.getCurrency("AUD")));
        boolean isSuccessful = bidModel.createBid(bid, jwt);
        assertTrue(isSuccessful);
    }

    @Test
    public void CreateBidNullBid() {
        boolean isSuccessful = bidModel.createBid(null, jwt);
        assertFalse(isSuccessful);
    }
}
