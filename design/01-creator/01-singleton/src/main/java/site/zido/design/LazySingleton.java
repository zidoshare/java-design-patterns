package site.zido.design;

import site.zido.design.base.InstanceCounter;

/**
 * 懒汉式单例
 * <p>
 * 类加载时没有生成单例，只有当第一次调用 getlnstance 方法时才去创建这个单例。
 */
public class LazySingleton extends InstanceCounter {

    private static volatile LazySingleton instance;

    private LazySingleton() {
    }

    /**
     * 方法级别的锁，每次获取instance都会需要获取锁，线程安全，但是效率较低，更优化的方案查看 {@link ThreadSafeSingleton.java}
     * @return
     */
    public static synchronized LazySingleton getInstance() {
        if (instance == null) {
            instance = new LazySingleton();
        }
        return instance;
    }
}