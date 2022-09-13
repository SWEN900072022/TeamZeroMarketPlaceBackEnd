package Mapper;

import Injector.FindConditionInjector;

import java.util.List;

public interface Mapper<T> {
    boolean insert(T TEntity);
    boolean delete(T TEntity);
    boolean modify(T TEntity);
    T find(FindConditionInjector injector, List<Object> queryParam);
    List<T> findMulti(FindConditionInjector injector, List<Object> queryParam);
}
