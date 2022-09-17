package MockClasses;

import Entity.Bid;
import Injector.FindConditionInjector;
import UnitofWork.IUnitofWork;
import org.javamoney.moneta.Money;

import javax.money.Monetary;
import java.util.List;

public class MockBidRepository implements IUnitofWork<Bid> {
    public boolean isLowBid = true;
    private Bid bid;

    public MockBidRepository() {
        bid = new Bid();
        bid.setBidAmount(Money.of(10.00, Monetary.getCurrency("AUD")));
    }

    @Override
    public Bid read(FindConditionInjector injector, List<Object> param, String key) {
        if(isLowBid) {
            bid.setBidAmount(Money.of(1.00, Monetary.getCurrency("AUD")));
        }
        return bid;
    }

    @Override
    public List<Bid> readMulti(FindConditionInjector injector, List<Object> param, String key) {
        return null;
    }

    @Override
    public Bid read(FindConditionInjector injector, List<Object> param) {
        if(isLowBid) {
            bid.setBidAmount(Money.of(1.00, Monetary.getCurrency("AUD")));
        }
        return bid;
    }

    @Override
    public List<Bid> readMulti(FindConditionInjector injector, List<Object> param) {
        return null;
    }

    @Override
    public void registerNew(Bid entity) {

    }

    @Override
    public void registerModified(Bid entity) {

    }

    @Override
    public void registerDeleted(Bid entity) {

    }

    @Override
    public void commit() {

    }
}
