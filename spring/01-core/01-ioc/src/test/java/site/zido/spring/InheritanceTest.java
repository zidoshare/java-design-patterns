package site.zido.spring;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import site.zido.spring.inheritance.DerivedTestBean;

public class InheritanceTest {
    @Test
    public void testInherit() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("inherits.xml");
        DerivedTestBean bean = context.getBean(DerivedTestBean.class);
        Assertions.assertEquals("override", bean.getName());
        Assertions.assertEquals(1, bean.getAge());
    }
}
