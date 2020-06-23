package site.zido.design;

import org.junit.jupiter.api.Test;

public class FlyweightTest {
    @Test
    public void test() {
        FlyweightFactory factory = new FlyweightFactory();
        Flyweight f01 = factory.getFlyweight("a");
        Flyweight f02 = factory.getFlyweight("a");
        Flyweight f03 = factory.getFlyweight("a");
        Flyweight f11 = factory.getFlyweight("b");
        Flyweight f12 = factory.getFlyweight("b");
        f01.operation(new UnSharedConcreteFlyweight("第1次调用a。"));
        f02.operation(new UnSharedConcreteFlyweight("第2次调用a。"));
        f03.operation(new UnSharedConcreteFlyweight("第3次调用a。"));
        f11.operation(new UnSharedConcreteFlyweight("第1次调用b。"));
        f12.operation(new UnSharedConcreteFlyweight("第2次调用b。"));
    }
}