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
        System.out.println("entering lock");
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
                wait(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(!lockMap.containsKey(compositeKey)){
            lockMap.put(compositeKey, owner);
            System.out.println("Lock successfully acquired for "+tableName);
        }else{
            System.out.println("Lock not acquired for " + tableName);
            throw new RuntimeException("Concurrency exception, " + owner + " could not acquire lock for " + tableName);
        }
    }

    public synchronized boolean releaseLock(String key,String tableName, String owner) {
        //Release a lock
        LockKey compositeKey = new LockKey(key, tableName);
        if(lockMap.get(compositeKey).equals(owner)){
            lockMap.remove(compositeKey);
            notifyAll();
            System.out.println("Lock successfully released for "+tableName);
            return true;
        }
        System.out.println("Lock not present for "+tableName);
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
