package site.zido.spring;

import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import site.zido.spring.callbacks.LifeCycleImpl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LifeCycleTest {
    @Test
    public void testLifeCycle() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("lifecycle.xml");
        LifeCycleImpl cycle = context.getBean(LifeCycleImpl.class);
        assertTrue(cycle.isRunning());
        context.close();
        assertFalse(cycle.isRunning());
    }
}
