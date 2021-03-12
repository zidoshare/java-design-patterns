package site.zido.design;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BridgeTest {
    @Test
    public void test() {
        ConcreteImplementorA implementorA = new ConcreteImplementorA();
        RefinedAbstraction refinedAbstraction = new RefinedAbstraction(implementorA);
        Assertions.assertEquals(2, refinedAbstraction.operation());
    }
}
