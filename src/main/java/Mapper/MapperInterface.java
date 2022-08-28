package Mapper;

import java.util.List;
import java.util.Map;

public interface MapperInterface<T> {
    boolean insert(List<T> TEntity);
    boolean delete(List<T> TEntity);
    boolean modify(List<T> TEntity);
    List<T> find(Map<String, String> map);
}
