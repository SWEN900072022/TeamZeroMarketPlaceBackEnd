package UnitofWork;

import java.util.Map;

public interface IUnitofWork<T>{
    Map<Integer, T> read(Integer[] id, String tableName);
    void registerNew(T entity);
    void registerModified(T entity);
    void registerDeleted(T entity);
    void commit();
}
