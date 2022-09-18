package Entity;

import Injector.FindConditionInjector.FindIdInjector;
import Mapper.SellerGroupMapper;
import UnitofWork.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SellerGroup extends EntityObject{
    private int groupId;
    private String groupName;

    public SellerGroup() {
        this.groupId = 0;
        this.groupName = "";
    }

    public SellerGroup(int groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
    }

    public SellerGroup(String groupName) {
        this.groupName = groupName;
        this.groupId = 0;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public boolean isEmpty() {
        if(Objects.equals(groupName, "")) {
            // Lazy load here.
            boolean canLoad = load();
            return !canLoad;
        }
        return false;
    }

    public boolean load() {
        // Check if the id is present, if not return false
        if(groupId == 0) {
            return false;
        }

        Repository<SellerGroup> sellerGroupRepo = new Repository<SellerGroup>(new SellerGroupMapper());
        List<Object> param = new ArrayList<>();
        param.add(groupId);
        SellerGroup sg = sellerGroupRepo.read(new FindIdInjector("sellergroup"), param);

        // populate the object with results
        groupName = sg.groupName;
        return true;
    }
}
