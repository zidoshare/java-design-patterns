package site.zido.design;

import org.junit.jupiter.api.Test;

public class CommandTest {
    @Test
    public void test() {
        Command cmd = new ConcreteCommand();
        Invoker ir = new Invoker(cmd);
        System.out.println("客户访问调用者的call()方法...");
        ir.call();
    }
}
