package UnitofWork;

import Enums.UnitActions;
import Injector.IInjector;
import Mapper.Mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Repository<T> implements IUnitofWork<T>{
    private final Map<String, List<T>> context;
    private final Mapper<T> mapper;
    private Map<String, T> identityMap;
    private Map<String, List<T>> OneToManyIdentityMap;

    public Repository(Mapper<T> mapper) {
        context = new HashMap<>();
        this.mapper = mapper;
        identityMap = new HashMap<>();
        OneToManyIdentityMap = new HashMap<>();
    }

    @Override
    public T read(IInjector injector, List<Object>param, String key) {
        if(identityMap.containsKey(key)) {
            return identityMap.get(key);
        } else {
            T entity = mapper.find(injector, param);
            identityMap.put(key, entity);
            return entity;
        }
    }

    @Override
    public List<T> readMulti(IInjector injector, List<Object>param, String key) {
        if(identityMap.containsKey(key)) {
            return OneToManyIdentityMap.get(key);
        } else {
            List<T> entity = mapper.findMulti(injector, param);
            OneToManyIdentityMap.put(key, entity);
            return entity;
        }
    }

    /*
    * This is not cached by the identity mapper
    * */
    @Override
    public T read(IInjector injector, List<Object>param) {
        return mapper.find(injector, param);
    }

    /*
    * This is not cached by the identity mapper
    * */
    @Override
    public List<T> readMulti(IInjector injector, List<Object>param) {
        return mapper.findMulti(injector, param);
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
