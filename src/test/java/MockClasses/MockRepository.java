package MockClasses;

import Container.DIContainer;
import Domain.*;
import Entity.EntityObject;
import Enums.UnitActions;
import Injector.ISQLInjector;
import Mapper.Mapper;
import UnitofWork.IUnitofWork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockRepository implements IUnitofWork {
    public boolean hasError = false;

    public Map<String, List<EntityObject>> testContext;
    private DIContainer<Mapper<EntityObject>> mapperDIContainer;

    public MockRepository() {
        testContext = new HashMap<>();
        try {
            mapperDIContainer = createContainer();
        } catch (Exception e) {
            mapperDIContainer = null;
        }
    }

    public Map<String, List<EntityObject>> getContext() {
        return testContext;
    }

    private DIContainer<Mapper<EntityObject>> createContainer() throws Exception {
        Map<Class<?>, Class<?>> mapperClass = new HashMap<>();

        // Register the mapper classes
        mapperClass.put(Bid.class, MockBidMapper.class);
        mapperClass.put(GroupMembership.class, MockGroupMembershipMapper.class);
        mapperClass.put(Listing.class, MockListingMapper.class);
        mapperClass.put(OrderItem.class, MockOrderItemMapper.class);
        mapperClass.put(Order.class, MockOrderMapper.class);
        mapperClass.put(SellerGroup.class, MockSellerGroupMapper.class);
        mapperClass.put(User.class, MockUserMapper.class);

        return new DIContainer<Mapper<EntityObject>>(mapperClass);
    }

    @Override
    public EntityObject read(ISQLInjector injector, List<Object> param, Class<?> serviceClass, String key) {
        return read(injector, param, serviceClass);
    }

    @Override
    public List<EntityObject> readMulti(ISQLInjector injector, List<Object> param, Class<?> serviceClass, String key) {
        return readMulti(injector, param, serviceClass);
    }

    @Override
    public EntityObject read(ISQLInjector injector, List<Object> param, Class<?> serviceClass) {
        // Look up the mappers
        Mapper<EntityObject> mapper = mapperDIContainer.getInstance(serviceClass.getCanonicalName());
        if(hasError) {
            return null;
        }
        return mapper.find(injector, param);
    }

    @Override
    public List<EntityObject> readMulti(ISQLInjector injector, List<Object> param, Class<?> serviceClass) {
        // Look up the mappers
        Mapper<EntityObject> mapper = mapperDIContainer.getInstance(serviceClass.getCanonicalName());
        if(hasError) {
            return null;
        }
        return mapper.findMulti(injector, param);
    }

    @Override
    public void registerNew(EntityObject entity) {
        register(entity, UnitActions.INSERT.toString());
    }

    @Override
    public void registerModified(EntityObject entity) {
        register(entity, UnitActions.MODIFY.toString());
    }

    @Override
    public void registerDeleted(EntityObject entity) {
        register(entity, UnitActions.DELETE.toString());
    }

    private void register(EntityObject entity, String operation) {
        List<EntityObject> entityToBeRegistered = testContext.get(operation);

        if(entityToBeRegistered == null) {
            entityToBeRegistered = new ArrayList<>();
        }

        entityToBeRegistered.add(entity);
        testContext.put(operation, entityToBeRegistered);
    }

    @Override
    public void commit() {
        // Check to see if there is anything to commit
        if(testContext.size() == 0) {
            return;
        }

        if(testContext.containsKey(UnitActions.INSERT.toString())) {
            commitNew();
        }

        if(testContext.containsKey(UnitActions.MODIFY.toString())) {
            commitModify();
        }

        if(testContext.containsKey(UnitActions.DELETE.toString())) {
            commitDelete();
        }
    }

    @Override
    public void rollback() {

    }

    private void commitNew() {
        List<EntityObject> entityList = testContext.get(UnitActions.INSERT.toString());
        for(EntityObject entity : entityList) {
            // Get the object key to determine the mapper to be ised
            Mapper<EntityObject> mapper = mapperDIContainer.getInstance(entity.getClass().getCanonicalName());
            mapper.insert(entity);
        }
    }

    private void commitModify() {
        List<EntityObject> entityList = testContext.get(UnitActions.MODIFY.toString());
        for(EntityObject entity : entityList) {
            Mapper<EntityObject> mapper = mapperDIContainer.getInstance(entity.getClass().getCanonicalName());
            mapper.modify(entity);
        }
    }

    private void commitDelete() {
        List<EntityObject> entityList = testContext.get(UnitActions.DELETE.toString());
        for(EntityObject entity : entityList) {
            Mapper<EntityObject> mapper = mapperDIContainer.getInstance(entity.getClass().getCanonicalName());
            mapper.delete(entity);
        }
    }
}
