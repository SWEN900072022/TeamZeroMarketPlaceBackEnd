package UnitofWork;

import Enums.UnitActions;
import Injector.FindIdInjector;
import Mapper.Mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Repository<T> implements IUnitofWork<T>{
    private final Map<String, List<T>> context;
    private final Mapper mapper;
    private Map<Integer, T> identityMap;

    public Repository(Mapper mapper) {
        context = new HashMap<>();
        this.mapper = mapper;
        identityMap = new HashMap<>();
    }

    @Override
    public Map<Integer, T> read(Integer[] idList, String tableName) {
        Map<Integer, T> result = new HashMap<>();

        for(Integer id : idList) {
            if(identityMap.containsKey(id)) {
                result.put(id, identityMap.get(id));
            } else {
                List<Object>param = new ArrayList<>();
                param.add(id);

                T entity = (T)mapper.find(new FindIdInjector(tableName), param);
                result.put(id, entity);
                identityMap.put(id, entity);
            }
        }
        return result;
    }

    @Override
    public void registerNew(T entity) {
        register(entity, UnitActions.INSERT.toString());
    }

    @Override
    public void registerModified(T entity) {
        register(entity, UnitActions.MODIFY.toString());
    }

    @Override
    public void registerDeleted(T entity) {
        register(entity, UnitActions.DELETE.toString());
    }

    private void register(T entity, String operation) {
        List<T> entityToBeRegistered = context.get(operation);
        if(entityToBeRegistered == null) {
            entityToBeRegistered = new ArrayList<>();
        }
        entityToBeRegistered.add(entity);
        context.put(operation, entityToBeRegistered);
    }

    @Override
    public void commit() {
        if(context.size() == 0) {
            return;
        }

        if(context.containsKey(UnitActions.INSERT.toString())) {
            commitNew();
        }

        if(context.containsKey(UnitActions.MODIFY.toString())) {
            commitModify();
        }

        if(context.containsKey(UnitActions.DELETE.toString())) {
            commitDel();
        }
    }

    private void commitNew() {
        List<T> entityList = context.get(UnitActions.INSERT.toString());
        for(T entity : entityList) {
            mapper.insert(entity);
        }
    }

    private void commitModify() {
        List<T> entityList = context.get(UnitActions.MODIFY.toString());
        for(T entity : entityList) {
            mapper.modify(entity);
        }
    }

    private void commitDel() {
        List<T> entityList = context.get(UnitActions.DELETE.toString());
        for(T entity : entityList) {
            mapper.delete(entity);
        }
    }

}
