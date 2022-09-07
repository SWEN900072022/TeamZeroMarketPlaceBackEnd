package UnitofWork;

import Entity.User;
import Enums.UnitActions;
import Mapper.UserMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRepository implements IUnitofWork<User>{

    private final Map<String, List<User>> context;
    private final UserMapper uMapper;

    public UserRepository() {
        context = new HashMap<>();
        uMapper = new UserMapper();
    }

    @Override
    public Map<Integer, User> read(Integer[] id) {
        return null;
    }

    @Override
    public void registerNew(User user) {
        register(user, UnitActions.INSERT.toString());
    }

    @Override
    public void registerModified(User user) {
        register(user, UnitActions.MODIFY.toString());
    }

    @Override
    public void registerDeleted(User user) {
        register(user, UnitActions.DELETE.toString());
    }

    private void register(User user, String operation) {
        List<User> userToBeRegistered = context.get(operation);
        if(userToBeRegistered == null) {
            userToBeRegistered = new ArrayList<>();
        }
        userToBeRegistered.add(user);
        context.put(operation, userToBeRegistered);
    }

    @Override
    public void commit() {
        if(context.size() == 0) {
            return;
        }

        if (context.containsKey(UnitActions.INSERT.toString())) {
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
        List<User> userList = context.get(UnitActions.INSERT.toString());
        uMapper.insert(userList);
    }

    private void commitModify() {
        List<User> userList = context.get(UnitActions.MODIFY.toString());
        uMapper.modify(userList);
    }

    private void commitDel() {
        List<User> userList = context.get(UnitActions.DELETE.toString());
        uMapper.delete(userList);
    }
}
