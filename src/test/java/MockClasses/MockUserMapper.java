package MockClasses;

import Entity.User;
import Enums.UserRoles;
import Injector.FindConditionInjector.FindEmailAndPasswordInjector;
import Injector.FindConditionInjector.FindUserOrEmailInjector;
import Injector.ISQLInjector;
import Mapper.Mapper;

import java.util.ArrayList;
import java.util.List;

public class MockUserMapper implements Mapper<User> {
    // In-memory database
    public List<User> users = new ArrayList<>();

    public MockUserMapper() {
        // Populate the users with some dummy data
        User user1 = new User();
        user1.setUserId(1);
        user1.setRole(UserRoles.CUSTOMER);
        user1.setUsername("a");
        user1.setPassword("a");
        user1.setEmail("a");

        User user2 = new User();
        user2.setUserId(2);
        user2.setRole(UserRoles.SELLER);
        user2.setUsername("b");
        user2.setPassword("b");
        user2.setEmail("b");

        users.add(user1);
        users.add(user2);
    }

    @Override
    public boolean insert(User TEntity) {
        users.add(TEntity);
        return true;
    }

    @Override
    public boolean delete(User TEntity) {
        users.removeIf(t -> (
                t.getUserId() == TEntity.getUserId()
                ));
        return true;
    }

    @Override
    public boolean modify(User TEntity) {
        delete(TEntity);
        insert(TEntity);
        return true;
    }

    @Override
    public User find(ISQLInjector injector, List<Object> queryParam) {
        for(User user : users) {
            if(injector instanceof FindUserOrEmailInjector) {
                if(queryParam.get(0) == user.getUsername()) {
                    return user;
                }
            }

            if(injector instanceof FindEmailAndPasswordInjector) {
                if(queryParam.get(0) == user.getEmail() &&
                        queryParam.get(1) == user.getPassword()
                ) {
                    return user;
                }
            }
        }

        return new User();
    }

    @Override
    public List<User> findMulti(ISQLInjector injector, List<Object> queryParam) {
        return users;
    }
}
