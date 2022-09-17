package Entity;

public class GroupMembership extends EntityObject{
    private int groupId;
    private int userId;

    public GroupMembership() {
        this.groupId = 0;
        this.userId = 0;
    }

    public GroupMembership(int groupId, int userId) {
        this.groupId = groupId;
        this.userId = userId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
