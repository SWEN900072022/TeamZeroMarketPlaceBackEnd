package Mapper;

import Injector.ISQLInjector;

import java.sql.Connection;
import java.util.List;

public interface Mapper<T> {
    boolean insert(T TEntity);
    boolean delete(T TEntity);
    boolean modify(T TEntity);
    T find(ISQLInjector injector, List<Object> queryParam);
    List<T> findMulti(ISQLInjector injector, List<Object> queryParam);
    void setConnection(Connection conn);
}
