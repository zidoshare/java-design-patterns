package site.zido.design;

import org.junit.jupiter.api.Test;

public class TemplateMethodTest {
    @Test
    public void test() {
        AbstractClass tm = new ConcreteClass();
        tm.templateMethod();
    }
}
