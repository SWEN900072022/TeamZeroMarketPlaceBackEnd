package MockClasses;

import Domain.SellerGroup;
import Injector.FindConditionInjector.FindGroupIdByNameInjector;
import Injector.ISQLInjector;
import Mapper.Mapper;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MockSellerGroupMapper implements Mapper<SellerGroup> {
    // In-memory database
    public List<SellerGroup> sgList = new ArrayList<>();

    public MockSellerGroupMapper() {
        // Populate the sgList with some dummy data
        SellerGroup sg1 = SellerGroup.create(
                1,
                "a"
        );

        SellerGroup sg2 = SellerGroup.create(
                2,
                "b"
        );

        sgList.add(sg1);
        sgList.add(sg2);
    }

    @Override
    public boolean insert(SellerGroup TEntity) {
        sgList.add(TEntity);
        return true;
    }

    @Override
    public boolean delete(SellerGroup TEntity) {
        sgList.removeIf(t -> (
                t.getGroupId() == TEntity.getGroupId()
                ));
        return true;
    }

    @Override
    public boolean modify(SellerGroup TEntity) {
        delete(TEntity);
        insert(TEntity);
        return false;
    }

    @Override
    public SellerGroup find(ISQLInjector injector, List<Object> queryParam) {
        for(SellerGroup sg : sgList) {
            if(injector instanceof FindGroupIdByNameInjector) {
                if(Objects.equals((String) queryParam.get(0), sg.getGroupName())) {
                    return sg;
                }
            }
        }
        return null;
    }

    @Override
    public List<SellerGroup> findMulti(ISQLInjector injector, List<Object> queryParam) {
        return sgList;
    }

    @Override
    public void setConnection(Connection conn) {

    }
}
