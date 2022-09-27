package Container;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DIContainer<T> {
    // There should be three stages tp the container
    private final Map<String, T> instances = new HashMap<>();

    // Register at the constructor
    public DIContainer(Map<Class<?>, Class<?>> instanceMapping) throws Exception {
        for(Class<?> key : instanceMapping.keySet()) {
            Class<?> instanceClass = instanceMapping.get(key);
            Constructor<?> constructor = instanceClass.getConstructor();
            constructor.setAccessible(true);
            T instance = (T)constructor.newInstance();
            instances.put(key.getCanonicalName(), instance);
        }
    }

    // Resolve
    public T getInstance(String key) {
        return instances.get(key);
    }

    // Release
}
