package site.zido.design;

import site.zido.design.base.InstanceCounter;

/**
 * 饿汉式单例
 */
public class HungrySingleton extends InstanceCounter {
    private static final HungrySingleton instance = new HungrySingleton();

    private HungrySingleton() {
    }

    public static HungrySingleton getInstance() {
        return instance;
    }
}