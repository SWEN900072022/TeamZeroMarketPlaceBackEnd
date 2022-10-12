package UnitofWork;

import Domain.EntityObject;
import Injector.ISQLInjector;

import java.sql.SQLException;
import java.util.List;

public interface IUnitofWork{
    EntityObject read(ISQLInjector injector, List<Object>param, Class<?>serviceClass, String key);
    List<EntityObject> readMulti(ISQLInjector injector, List<Object>param, Class<?>serviceClass, String key);
    EntityObject read(ISQLInjector injector, List<Object>param, Class<?>serviceClass);
    List<EntityObject> readMulti(ISQLInjector injector, List<Object>param, Class<?>serviceClass);
    void registerNew(EntityObject entity);
    void registerModified(EntityObject entity);
    void registerDeleted(EntityObject entity);
    void commit() throws SQLException;
    void rollback();
}
