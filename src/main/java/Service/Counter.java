package Service;

import Domain.Order;

import java.util.HashMap;
import java.util.Map;

public class Counter {
    public static Map<Class<?>, Integer> CounterMap = new HashMap();

    public static void init() {
        CounterMap.put(Order.class, 100);
    }

    public static int increment(Class<?> clazz) {
        int val = CounterMap.get(clazz);
        CounterMap.put(clazz, val + 1);
        return val+1;
    }
}
