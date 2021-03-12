package site.zido.design;

import site.zido.design.base.InstanceCounter;

/**
 * 线程安全且效率高的懒汉模式
 */
public class ThreadSafeSingleton extends InstanceCounter {
    private static ThreadSafeSingleton instance;

    private ThreadSafeSingleton() {

    }

    public static ThreadSafeSingleton getInstance() {
        //使用两层检测，以消除锁带来的额外开销
        if (instance == null) {
            synchronized (ThreadSafeSingleton.class) {
                if (instance == null) {
                    instance = new ThreadSafeSingleton();
                }
            }
        }
        return instance;
    }
}