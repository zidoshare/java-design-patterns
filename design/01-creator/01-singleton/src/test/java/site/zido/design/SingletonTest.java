package site.zido.design;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import site.zido.design.base.InstanceCounter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class SingletonTest {
    /**
     * 懒汉式单例
     */
    @Test
    public void lazy() {
        Assertions.assertEquals(0, InstanceCounter.getInstanceCount(LazySingleton.class), "延迟初始化，直到真正需要实例时才会初始化");
        for (int i = 0; i < 10; i++) {
            LazySingleton.getInstance();
            Assertions.assertEquals(1, InstanceCounter.getInstanceCount(LazySingleton.class), "仅会初始化一次");
        }
    }

    /**
     * 饿汉式单例
     */
    @Test
    public void hungry() {
        Assertions.assertEquals(1, InstanceCounter.getInstanceCount(HungrySingleton.class), "预先便会被初始化");
        for (int i = 0; i < 10; i++) {
            HungrySingleton.getInstance();
            Assertions.assertEquals(1, InstanceCounter.getInstanceCount(HungrySingleton.class), "仅会初始化一次");
        }
    }

    /**
     * 线程安全且效率高的懒汉模式
     *
     * @throws InterruptedException
     */
    @Test
    public void threadSafeLazy() throws InterruptedException {
        final int count = 1000;
        CountDownLatch latch = new CountDownLatch(1);
        CountDownLatch taskLatch = new CountDownLatch(count);
        List<Exception> errorList = new ArrayList<>();
        Assertions.assertEquals(0, InstanceCounter.getInstanceCount(ThreadSafeSingleton.class), "延迟初始化，直到真正需要实例时才会初始化");
        for (int i = 0; i < count; i++) {
            new Thread(() -> {
                try {
                    latch.await();
                    ThreadSafeSingleton.getInstance();
                    if (InstanceCounter.getInstanceCount(ThreadSafeSingleton.class) != 1) {
                        errorList.add(new RuntimeException("初始化多余一次"));
                    }
                } catch (InterruptedException e) {
                    errorList.add(e);
                } finally {
                    taskLatch.countDown();
                }
            }).start();
        }
        latch.countDown();
        taskLatch.await();
        Assertions.assertEquals(0, errorList.size(), "不可能出现异常");
        for (Exception exception : errorList) {
            exception.printStackTrace();
        }
    }
}
