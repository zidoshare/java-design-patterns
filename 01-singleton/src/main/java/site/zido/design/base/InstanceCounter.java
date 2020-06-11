package site.zido.design.base;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 实例计数
 * <p>
 * 线程安全
 */
public abstract class InstanceCounter {
    private static final ConcurrentHashMap<Class<?>, Integer> INSTANCE_COUNT = new ConcurrentHashMap<>(3);
    private static final ConcurrentHashMap<Class<?>, Boolean> LOAD_FLAG_CONTAINER = new ConcurrentHashMap<>(3);

    public InstanceCounter() {
        INSTANCE_COUNT.compute(getClass(), (key, val) -> Optional.ofNullable(val).orElse(0) + 1);
    }

    public final int getInstanceCount() {
        return INSTANCE_COUNT.getOrDefault(getClass(), 0);
    }

    public static int getInstanceCount(final Class<?> clazz) {
        // 为了保证准确性，静态方法调用会保证类已经被加载
        // 因为没有找到判断类是否被加载的api，暂时采用标记的方式
        //线程安全
        LOAD_FLAG_CONTAINER.computeIfAbsent(clazz, key -> {
            try {
                Class.forName(key.getName());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("加载class失败", e);
            }
            return true;
        });
        return INSTANCE_COUNT.getOrDefault(clazz, 0);
    }
}
