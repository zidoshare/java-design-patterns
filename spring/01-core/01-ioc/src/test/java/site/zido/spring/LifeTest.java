package site.zido.spring;

import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import site.zido.spring.life.AnnotationBean;
import site.zido.spring.life.DisposableBeanImpl;
import site.zido.spring.life.InitBean;
import site.zido.spring.life.InitializingBeanImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 生命周期相关测试
 */
public class LifeTest {

    /**
     * 测试InitializingBean和DisposableBean接口
     */
    @Test
    public void testInitializingBean() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("initializing-bean.xml");

        InitializingBeanImpl bean = context.getBean(InitializingBeanImpl.class);
        assertEquals("after properties", bean.getName());

        DisposableBeanImpl bean2 = context.getBean(DisposableBeanImpl.class);
        assertEquals("before property", bean2.getName());
        context.close();
        assertEquals("before destroy", bean2.getName());
    }

    @Test
    public void testDefaultInitAndDestroyMethod() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("method-bean.xml");

        InitBean obj = context.getBean(InitBean.class);
        assertEquals("init", obj.getName());
        context.close();
        assertEquals("destroy", obj.getName());
    }

    @Test
    public void testAnnotation() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("annotation-bean.xml");

        AnnotationBean obj = context.getBean(AnnotationBean.class);
        assertEquals("init", obj.getName());
        context.close();
        assertEquals("destroy", obj.getName());
    }
}
