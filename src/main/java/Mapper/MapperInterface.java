package Mapper;

import java.util.List;
import java.util.Map;

public interface MapperInterface<T> {
    boolean insert(T TEntity);
    boolean delete(T TEntity);
    boolean modify(T TEntity);
    List<T> find(Map<String, String> map);
}
