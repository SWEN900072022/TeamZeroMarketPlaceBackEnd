package UnitofWork;

import Container.DIContainer;
import Domain.*;
import Enums.UnitActions;
import Injector.ISQLInjector;
import Mapper.*;
import Util.SQLUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class UnitofWork implements IUnitofWork{
    private final Map<String, List<EntityObject>> context;
    private Map<String, EntityObject> oneToOneIdentityMap;
    private Map<String, List<EntityObject>> oneToManyIdentityMap;
    private DIContainer<Mapper<EntityObject>> mapperContainer;

    public UnitofWork() {
        context = new HashMap<>();
        oneToOneIdentityMap = new HashMap<>();
        oneToManyIdentityMap = new HashMap<>();
        try {
            mapperContainer = createContainer();
        } catch (Exception e) {
            mapperContainer = null;
        }
    }

    private DIContainer<Mapper<EntityObject>> createContainer() throws Exception {
        Map<Class<?>, Class<?>> mapperClasses = new HashMap();

        // Register the mapper classes
        mapperClasses.put(Bid.class, BidMapper.class);
        mapperClasses.put(GroupMembership.class, GroupMembershipMapper.class);
        mapperClasses.put(Listing.class, ListingMapper.class);
        mapperClasses.put(OrderItem.class, OrderItemMapper.class);
        mapperClasses.put(Order.class, OrderMapper.class);
        mapperClasses.put(SellerGroup.class, SellerGroupMapper.class);
        mapperClasses.put(User.class, UserMapper.class);

        return new DIContainer(mapperClasses);
    }

    @Override
    public EntityObject read(ISQLInjector injector, List<Object> param, Class<?>objClass, String key) {
        // First check to see if the entity object is in the identity map
        if(oneToOneIdentityMap.containsKey(key)) {
            // Return the object in the identity map
            return oneToOneIdentityMap.get(key);
        } else {
            Connection conn = SQLUtil.getInstance().getConnection();
            // look up the database and return the object
            // First, we need to lookup for the correct mapper
            Mapper<EntityObject> mapper = mapperContainer.getInstance(objClass.getCanonicalName());
            mapper.setConnection(conn);

            EntityObject entity = (EntityObject) mapper.find(injector, param);
            oneToOneIdentityMap.put(key, entity);
            SQLUtil.getInstance().close(conn);
            return entity;
        }
    }

    @Override
    public List<EntityObject> readMulti(ISQLInjector injector, List<Object> param, Class<?>objClass, String key) {
        // First check to see if the entity object is in the identity map
        if(oneToManyIdentityMap.containsKey(key)) {
            // Return the object in the identity map
            return oneToManyIdentityMap.get(key);
        } else {
            Connection conn = SQLUtil.getInstance().getConnection();
            // look up the database and return the relevant mapper
            // First, we need to lookup the correct mapper
            Mapper<EntityObject> mapper = mapperContainer.getInstance(objClass.getCanonicalName());
            mapper.setConnection(conn);

            List<EntityObject> entity = mapper.findMulti(injector, param);
            oneToManyIdentityMap.put(key, entity);
            SQLUtil.getInstance().close(conn);
            return entity;
        }
    }

    @Override
    public EntityObject read(ISQLInjector injector, List<Object> param, Class<?>objClass) {
        // Look up the database and return the relevant mapper
        Connection conn = SQLUtil.getInstance().getConnection();
        Mapper<EntityObject> mapper = mapperContainer.getInstance(objClass.getCanonicalName());
        mapper.setConnection(conn);

        EntityObject obj = mapper.find(injector, param);
        SQLUtil.getInstance().close(conn);
        return obj;
    }

    @Override
    public List<EntityObject> readMulti(ISQLInjector injector, List<Object> param, Class<?>objClass) {
        // Look up the database and return the relevant mappers
        Connection conn = SQLUtil.getInstance().getConnection();
        Mapper<EntityObject> mapper = mapperContainer.getInstance(objClass.getCanonicalName());
        mapper.setConnection(conn);

        List<EntityObject> objList = mapper.findMulti(injector, param);
        SQLUtil.getInstance().close(conn);
        return objList;
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
        List<EntityObject> entityToBeRegistered = context.get(operation);

        if(entityToBeRegistered == null) {
            entityToBeRegistered = new ArrayList<>();
        }

        entityToBeRegistered.add(entity);
        context.put(operation, entityToBeRegistered);
    }

    @Override
    public void commit() throws SQLException, InterruptedException{
        Connection conn = SQLUtil.getInstance().getConnection();
        // Check to see if there are anything to commit
        if(context.size() == 0) {
            SQLUtil.getInstance().close(conn);
            return;
        }

        if(context.containsKey(UnitActions.INSERT.toString())) {
            commitNew(conn);
        }

        if(context.containsKey(UnitActions.MODIFY.toString())) {
            commitModify(conn);
        }

        if(context.containsKey(UnitActions.DELETE.toString())) {
            commitDelete(conn);
        }

        // Everything that needs to be commited should be commited at this point
        try{
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
        } finally {
            SQLUtil.getInstance().close(conn);
        }
    }

    private void commitNew(Connection conn) {
        List<EntityObject> entityList = context.get(UnitActions.INSERT.toString());
        for(EntityObject entity : entityList) {
            // Get the object key to determine the mapper to be used
            // Once we have the mapper, we can start inserting it into the db
            Mapper<EntityObject> mapper = mapperContainer.getInstance(entity.getClass().getCanonicalName());
            if(mapper == null) {
                // Get super class name instead
                mapper = mapperContainer.getInstance(entity.getClass().getSuperclass().getCanonicalName());
            }
            mapper.setConnection(conn);
            mapper.insert(entity);
        }
    }

    private void commitModify(Connection conn) {
        List<EntityObject> entityList = context.get(UnitActions.MODIFY.toString());
        for(EntityObject entity : entityList) {
            Mapper<EntityObject> mapper = mapperContainer.getInstance(entity.getClass().getCanonicalName());
            if(mapper == null) {
                // Get super class name instead
                mapper = mapperContainer.getInstance(entity.getClass().getSuperclass().getCanonicalName());
            }
            mapper.setConnection(conn);
            mapper.modify(entity);
        }
    }

    private void commitDelete(Connection conn) {
        List<EntityObject> entityList = context.get(UnitActions.DELETE.toString());
        for(EntityObject entity : entityList) {
            Mapper<EntityObject> mapper = mapperContainer.getInstance(entity.getClass().getCanonicalName());
            if(mapper == null) {
                // Get super class name instead
                mapper = mapperContainer.getInstance(entity.getClass().getSuperclass().getCanonicalName());
            }
            mapper.setConnection(conn);
            mapper.delete(entity);
        }
    }
}
