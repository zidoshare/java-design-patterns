package site.zido.design;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import site.zido.design.base.InstanceCounter;

public class SingletonTest {
    /**
     * 懒汉式单例
     */
    @Test
    public void lazy() {
        Assert.assertEquals("延迟初始化，直到真正需要实例时才会初始化", 0, InstanceCounter.getInstanceCount(LazySingleton.class));
        for (int i = 0; i < 10; i++) {
            LazySingleton.getInstance();
            Assert.assertEquals("仅会初始化一次", 1, InstanceCounter.getInstanceCount(LazySingleton.class));
        }
    }

    /**
     * 饿汉式单例
     */
    @Test
    public void hungry() {
        Assert.assertEquals("预先便会被初始化", 1, InstanceCounter.getInstanceCount(HungrySingleton.class));
        for (int i = 0; i < 10; i++) {
            HungrySingleton.getInstance();
            Assert.assertEquals("仅会初始化一次", 1, InstanceCounter.getInstanceCount(HungrySingleton.class));
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
        Assert.assertEquals("延迟初始化，直到真正需要实例时才会初始化", 0, InstanceCounter.getInstanceCount(ThreadSafeSingleton.class));
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
        Assert.assertEquals("不可能出现异常", 0, errorList.size());
        for (Exception exception : errorList) {
            exception.printStackTrace();
        }
    }
}
