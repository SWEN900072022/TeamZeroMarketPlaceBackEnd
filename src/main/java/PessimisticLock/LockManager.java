package PessimisticLock;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.Timer;

public class LockManager {
    private static LockManager instance;

    //Key: <Key,TableName>
    //Value: owner
    private ConcurrentMap<LockKey, String> lockMap;

    public static synchronized LockManager getInstance() {
        if(instance == null) {
            instance = new LockManager();
        }
        return instance;
    }

    private LockManager() {
        lockMap = new ConcurrentHashMap<LockKey, String>();
    }

    public synchronized void acquireLock(String key,String tableName, String owner) {
        LockKey compositeKey = new LockKey(key, tableName);
        long startTime = System.currentTimeMillis();
        long elapsedTime = 0;

        while(lockMap.containsKey(compositeKey)) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        lockMap.put(compositeKey, owner);

//        if(!lockMap.containsKey(compositeKey)) {
//            //no lock on lockable, grant lock
//            lockMap.put(compositeKey, owner);
//            return true;
//        } else {
//            throw new RuntimeException("Concurrency exception, " + owner + " could not acquire lock for " + compositeKey.getKey()+compositeKey.getTableName());
//        }
    }

    public synchronized boolean releaseLock(String key,String tableName, String owner) {
        //Release a lock
        LockKey compositeKey = new LockKey(key, tableName);
        if(lockMap.get(compositeKey).equals(owner)){
            lockMap.remove(compositeKey);
            notifyAll();
            return true;
        }
        return false;

    }

    public synchronized void releaseOwner(String owner) {
        //Release all locks held by owner
        for(LockKey compositeKey:lockMap.keySet()){
            if(lockMap.get(compositeKey).equals(owner)){
                lockMap.remove(compositeKey);
            }
        }
        notifyAll();
    }
}
