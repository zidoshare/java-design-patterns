package site.zido.spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpringTest {
    /**
     * xml配置测试
     */
    @Test
    public void testWithXml() {
        ApplicationContext context = new ClassPathXmlApplicationContext("xml-test.xml");

        XmlBeanOne one = context.getBean("one", XmlBeanOne.class);
        assertEquals("a", one.getName());
        assertEquals("b", one.getValue());

        XmlBeanTwo two = context.getBean("two", XmlBeanTwo.class);
        assertEquals("a", two.getName());
        assertEquals("b", two.getEmail());
    }

    @Test
    public void testComposingXml() {
        ApplicationContext context = new ClassPathXmlApplicationContext("compose-test.xml");

        XmlBeanOne one = context.getBean("one", XmlBeanOne.class);
        assertEquals("a", one.getName());
        assertEquals("b", one.getValue());

        XmlBeanTwo two = context.getBean("two", XmlBeanTwo.class);
        assertEquals("a", two.getName());
        assertEquals("b", two.getEmail());

        one = context.getBean("one-one", XmlBeanOne.class);
        assertEquals("1", one.getName());
        assertEquals("2", one.getValue());
    }

    @Test
    public void testGenericApplicationContext() {
        GenericApplicationContext context = new GenericApplicationContext();
        new XmlBeanDefinitionReader(context).loadBeanDefinitions("xml-test.xml");
        context.refresh();

        XmlBeanOne one = context.getBean("one", XmlBeanOne.class);
        assertEquals("a", one.getName());
        assertEquals("b", one.getValue());

        XmlBeanTwo two = context.getBean("two", XmlBeanTwo.class);
        assertEquals("a", two.getName());
        assertEquals("b", two.getEmail());
    }
}
