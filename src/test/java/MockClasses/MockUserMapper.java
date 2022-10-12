package MockClasses;

import Domain.User;
import Enums.UserRoles;
import Injector.FindConditionInjector.FindEmailAndPasswordInjector;
import Injector.FindConditionInjector.FindUserOrEmailInjector;
import Injector.ISQLInjector;
import Mapper.Mapper;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class MockUserMapper implements Mapper<User> {
    // In-memory database
    public List<User> users = new ArrayList<>();

    public MockUserMapper() {
        // Populate the users with some dummy data
        User user1 = User.create(
                "a",
                "a",
                "a",
                1,
                UserRoles.CUSTOMER.toString()
        );

        User user2 = User.create(
                "b",
                "b",
                "b",
                2,
                UserRoles.SELLER.toString()
        );

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

        return null;
    }

    @Override
    public List<User> findMulti(ISQLInjector injector, List<Object> queryParam) {
        return users;
    }

    @Override
    public void setConnection(Connection conn) {

    }
}
