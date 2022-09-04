package Mapper;

import java.util.List;
import java.util.Map;

public interface MapperInterface<T> {
    boolean insert(List<T> TEntity);
    boolean delete(List<T> TEntity);
    boolean modify(List<T> TEntity);
    Map<Integer, T> find(Map<String, String> map);
    Map<Integer, T> find(Map<String, String> map, int mode);
}
