package MockClasses;

import Entity.Bid;
import Injector.ISQLInjector;
import Mapper.Mapper;
import org.javamoney.moneta.Money;

import javax.money.Monetary;
import java.util.ArrayList;
import java.util.List;

public class MockBidMapper implements Mapper<Bid> {
    // In-memory database
    public List<Bid> bids = new ArrayList<>();

    public MockBidMapper() {
        // Populate the bids with some dummy data
        Bid bid1 = new Bid();
        bid1.setBidAmount(Money.of(10, Monetary.getCurrency("AUD")));
        bid1.setUserId(1);
        bid1.setListingId(1);

        Bid bid2 = new Bid();
        bid2.setBidAmount(Money.of(20, Monetary.getCurrency("AUD")));
        bid2.setUserId(2);
        bid2.setListingId(2);

        bids.add(bid1);
        bids.add(bid2);
    }
    @Override
    public boolean insert(Bid TEntity) {
        bids.add(TEntity);
        return true;
    }

    @Override
    public boolean delete(Bid TEntity) {
        bids.removeIf(t -> (
                t.getListingId() == TEntity.getListingId() &&
                        t.getUserId() == TEntity.getUserId()
        ));
        return true;
    }

    @Override
    public boolean modify(Bid TEntity) {
        delete(TEntity);
        insert(TEntity);
        return false;
    }

    @Override
    public Bid find(ISQLInjector injector, List<Object> queryParam) {
        return bids.get(0);
    }

    @Override
    public List<Bid> findMulti(ISQLInjector injector, List<Object> queryParam) {
        return bids;
    }
}
