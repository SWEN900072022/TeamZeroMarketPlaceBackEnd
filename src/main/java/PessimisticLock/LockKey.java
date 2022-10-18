package PessimisticLock;

public class LockKey {
    private String key;
    private String tableName;

    public LockKey(String key, String tableName){
        this.key=key;
        this.tableName=tableName;
    }
    public String getKey() {
        return key;
    }

    public String getTableName() {
        return tableName;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj != null && obj instanceof LockKey) {
            LockKey s = (LockKey)obj;
            return key.equals(s.key) && tableName.equals(s.tableName);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (key + tableName).hashCode();
    }
}