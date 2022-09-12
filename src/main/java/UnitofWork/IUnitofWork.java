package UnitofWork;

import Injector.FindConditionInjector;

import java.util.List;
import java.util.Map;

public interface IUnitofWork<T>{
    T read(FindConditionInjector injector, List<Object>param, String key);
    List<T> readMulti(FindConditionInjector injector, List<Object>param, String key);
    T read(FindConditionInjector injector, List<Object>param);
    List<T> readMulti(FindConditionInjector injector, List<Object>param);
    void registerNew(T entity);
    void registerModified(T entity);
    void registerDeleted(T entity);
    void commit();
}
