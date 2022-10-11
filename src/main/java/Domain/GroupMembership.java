package Domain;

public class GroupMembership {
    private int groupId;
    private int userId;

    protected GroupMembership(int groupId, int userId) {
        this.groupId = groupId;
        this.userId = userId;
    }

    public static GroupMembership create(int groupId, int userId) {
        return new GroupMembership(groupId, userId);
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
