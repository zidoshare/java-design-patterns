package site.zido.jvm;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class HeapOOMTest {
    /**
     * 堆内存是 JVM 所有线程共享的部分，在虚拟机启动的时候就已经创建。
     * 所有的对象和数组都在堆上进行分配。
     * 这部分空间可通过 GC 进行回收。
     * 当申请不到空间时会抛出 OutOfMemoryError。
     */
    @Test
    public void testOOM() {
        List<byte[]> list = new ArrayList<byte[]>();
        int i = 0;
        boolean flag = true;
        while (flag) {
            try {
                i++;
                list.add(new byte[1024 * 1024]);//每次增加一个1M大小的数组对象
            } catch (Throwable e) {
                Assertions.assertEquals(OutOfMemoryError.class, e.getClass());
                e.printStackTrace();
                flag = false;
                System.out.println("count=" + i);//记录运行的次数
            }
        }
    }

    static String base = "string";

    /**
     * 绝大部分 Java 程序员应该都见过 "java.lang.OutOfMemoryError: PermGen space "这个异常。
     * 这里的 “PermGen space”其实指的就是方法区。
     * 不过方法区和“PermGen space”又有着本质的区别。
     * 前者是 JVM 的规范，而后者则是 JVM 规范的一种实现，并且只有 HotSpot 才有 “PermGen space”，
     * 而对于其他类型的虚拟机，如 JRockit（Oracle）、J9（IBM） 并没有“PermGen space”。
     * 由于方法区主要存储类的相关信息，所以对于动态生成类的情况比较容易出现永久代的内存溢出。
     * 最典型的场景就是，在 jsp 页面比较多的情况，容易出现永久代内存溢出。
     *
     *
     * <b>注意</b>： jdk8中没有永久代，只有元空间，不会产生java.lang.OutOfMemoryError: PermGen space
     * <p>
     * 测试没法运行，需要使用main方法，使用命令运行才能看到元空间溢出
     */
    @Test
    public void testPermGenOOM() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            String str = base + base;
            base = str;
            list.add(str.intern());
        }
    }
}
