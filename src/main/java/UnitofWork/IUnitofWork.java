package UnitofWork;

import Injector.IInjector;

import java.util.List;

public interface IUnitofWork<T>{
    T read(IInjector injector, List<Object>param, String key);
    List<T> readMulti(IInjector injector, List<Object>param, String key);
    T read(IInjector injector, List<Object>param);
    List<T> readMulti(IInjector injector, List<Object>param);
    void registerNew(T entity);
    void registerModified(T entity);
    void registerDeleted(T entity);
    void commit();
}
