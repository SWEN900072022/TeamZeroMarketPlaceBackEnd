package Mapper;

import Injector.IInjector;

import java.util.List;

public interface Mapper<T> {
    boolean insert(T TEntity);
    boolean delete(T TEntity);
    boolean modify(T TEntity);
    T find(IInjector injector, List<Object> queryParam);
    List<T> findMulti(IInjector injector, List<Object> queryParam);
}
