package site.zido.jvm;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StackErrorTest {

    static class StackErrorMock {
        private static int index = 1;

        public void call() {
            index++;
            call();
        }
    }

    @Test
    public void testStackError() {
        StackErrorMock mock = new StackErrorMock();
        try {
            mock.call();
        } catch (Throwable e) {
            //每次运行栈的深度都是不一样的
            Assertions.assertEquals(StackOverflowError.class, e.getClass());
            System.out.println("Stack deep : " + mock.index);
            e.printStackTrace();
        }
    }
}
