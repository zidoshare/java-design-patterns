package site.zido.sync;

public class SynchronizedDemo {

    private static final Object STATIC_MONITOR = new Object();
    private final Object object_monitor = new Object();

    public static synchronized void staticMethod() throws InterruptedException {
        System.out.println("静态同步方法开始");
        Thread.sleep(1000);
        System.out.println("静态同步方法结束");
    }

    public synchronized void method() throws InterruptedException {
        System.out.println("实例同步方法开始");
        Thread.sleep(1000);
        System.out.println("实例同步方法结束");
    }

    public synchronized void method2() throws InterruptedException {
        System.out.println("实例同步方法2开始");
        Thread.sleep(3000);
        System.out.println("实例同步方法2结束");
    }

    public void blockStaticMethod1() throws InterruptedException {
        synchronized (STATIC_MONITOR) {
            System.out.println("静态对象同步方法1开始");
            Thread.sleep(3000);
            System.out.println("静态对象同步方法1结束");
        }
    }

    public void blockStaticMethod2() throws InterruptedException {
        synchronized (STATIC_MONITOR) {
            System.out.println("静态对象同步方法2开始");
            Thread.sleep(3000);
            System.out.println("静态对象同步方法2结束");
        }
    }

    public void blockMethod1() throws InterruptedException {
        synchronized (object_monitor) {
            System.out.println("类属性对象同步方法2开始");
            Thread.sleep(3000);
            System.out.println("类属性对象同步方法2结束");
        }
    }

    public void blockMethod2() throws InterruptedException {
        synchronized (object_monitor) {
            System.out.println("类属性对象同步方法2开始");
            Thread.sleep(3000);
            System.out.println("类属性对象同步方法2结束");
        }
    }

    public static void main(String[] args) {

        //静态同步方法
        new Thread(() -> {
            try {
                SynchronizedDemo.staticMethod();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                SynchronizedDemo.staticMethod();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        final SynchronizedDemo synDemo = new SynchronizedDemo();
        //实例同步方法
        Thread thread1 = new Thread(() -> {
            try {
                synDemo.method();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread thread2 = new Thread(() -> {
            try {
                synDemo.method2();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread1.start();
        thread2.start();

        //静态属性同步代码块
        new Thread(() -> {
            try {
                synDemo.blockStaticMethod1();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                synDemo.blockStaticMethod2();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        //实例属性同步代码块
        new Thread(() -> {
            try {
                synDemo.blockMethod1();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                synDemo.blockMethod1();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
