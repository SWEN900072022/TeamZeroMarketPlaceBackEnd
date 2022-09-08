package UnitofWork;

public interface IUnitofWork<T>{
    void registerNew(T entity);
    void registerModified(T entity);
    void registerDeleted(T entity);
    void commit() throws Exception;
}
