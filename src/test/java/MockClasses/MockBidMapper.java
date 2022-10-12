package MockClasses;

import Domain.Bid;
import Injector.ISQLInjector;
import Mapper.Mapper;
import org.javamoney.moneta.Money;

import javax.money.Monetary;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class MockBidMapper implements Mapper<Bid> {
    // In-memory database
    public List<Bid> bids = new ArrayList<>();

    public MockBidMapper() {
        // Populate the bids with some dummy data
        Bid bid1 = Bid.create(
                1,
                1,
                Money.of(10, Monetary.getCurrency("AUD"))
        );

        Bid bid2 = Bid.create(
                2,
                2,
                Money.of(20, Monetary.getCurrency("AUD"))
        );

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

    @Override
    public void setConnection(Connection conn) {

    }
}
