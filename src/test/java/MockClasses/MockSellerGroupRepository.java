package MockClasses;

import Entity.SellerGroup;
import Injector.IInjector;
import UnitofWork.IUnitofWork;

import java.util.List;

public class MockSellerGroupRepository implements IUnitofWork<SellerGroup> {
    public boolean isEmpty = false;
    private SellerGroup sg;

    public MockSellerGroupRepository() {
        sg = new SellerGroup();
        sg.setGroupId(1);
        sg.setGroupName("a");
    }

    @Override
    public SellerGroup read(IInjector injector, List<Object> param, String key) {
        return null;
    }

    @Override
    public List<SellerGroup> readMulti(IInjector injector, List<Object> param, String key) {
        return null;
    }

    @Override
    public SellerGroup read(IInjector injector, List<Object> param) {
        if(isEmpty) {
            return new SellerGroup();
        } else {
            return sg;
        }
    }

    @Override
    public List<SellerGroup> readMulti(IInjector injector, List<Object> param) {
        return null;
    }

    @Override
    public void registerNew(SellerGroup entity) {

    }

    @Override
    public void registerModified(SellerGroup entity) {

    }

    @Override
    public void registerDeleted(SellerGroup entity) {

    }

    @Override
    public void commit() {

    }
}
