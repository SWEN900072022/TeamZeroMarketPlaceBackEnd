package Entity;

public class SellerGroup extends EntityObject{
    private int groupId;
    private String groupName;

    public SellerGroup() {
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
        if(groupId == 0) {
            return true;
        }
        return false;
    }
}
