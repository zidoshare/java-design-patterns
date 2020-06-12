package site.zido.design;

import org.junit.Assert;
import org.junit.Test;

public class BridgeTest {
    @Test
    public void test() {
        ConcreteImplementorA implementorA = new ConcreteImplementorA();
        RefinedAbstraction refinedAbstraction = new RefinedAbstraction(implementorA);
        Assert.assertEquals(2, refinedAbstraction.operation());
    }
}
