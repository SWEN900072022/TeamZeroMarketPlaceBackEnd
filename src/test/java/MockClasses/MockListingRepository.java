package MockClasses;

import Entity.Listing;
import Injector.FindConditionInjector;
import UnitofWork.IUnitofWork;

import java.util.List;

public class MockListingRepository implements IUnitofWork<Listing> {
    private Listing listing = new Listing();
    public boolean isNull = false;
    public boolean commitException = false;

    public MockListingRepository() {
        listing = new Listing();
        listing.setListingId(1);
        listing.setQuantity(10);
    }

    @Override
    public Listing read(FindConditionInjector injector, List<Object> param, String key) {
        if(isNull) {
            return null;
        }

        return listing;
    }

    @Override
    public List<Listing> readMulti(FindConditionInjector injector, List<Object> param, String key) {
        return null;
    }

    @Override
    public Listing read(FindConditionInjector injector, List<Object> param) {
        if(isNull) {
            return null;
        }

        return listing;
    }

    @Override
    public List<Listing> readMulti(FindConditionInjector injector, List<Object> param) {
        return null;
    }

    @Override
    public void registerNew(Listing entity) {

    }

    @Override
    public void registerModified(Listing entity) {

    }

    @Override
    public void registerDeleted(Listing entity) {

    }

    @Override
    public void commit() {
    }
}
