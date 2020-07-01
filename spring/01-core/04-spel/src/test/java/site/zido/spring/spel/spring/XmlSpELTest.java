package site.zido.spring.spel.spring;

import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class XmlSpELTest {
    /**
     * 在spring环境中使用xml SpEL
     */
    @Test
    public void testXmlSpEL() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("sp1.xml");
        context.refresh();
        NumberGuess guess = context.getBean(NumberGuess.class);
        assertNotNull(guess.getRandomNumber());
    }

    /**
     * 使用环境变量
     */
    @Test
    public void testWithSystemProperties() {
        System.setProperty("user.region", "abc");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("sp2.xml");
        context.refresh();
        TaxCalculator taxCalculator = context.getBean(TaxCalculator.class);
        System.out.println(taxCalculator.getDefaultLocale());
        assertEquals("abc", taxCalculator.getDefaultLocale());
    }

    /**
     * 测试获取其他bean的属性
     */
    @Test
    public void testGetOtherBeanProperties() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("sp3.xml");
        context.refresh();
        ShapeGuess shapeGuess = context.getBean(ShapeGuess.class);
        NumberGuess numberGuess = context.getBean(NumberGuess.class);
        assertNotNull(numberGuess.getRandomNumber());
        assertEquals(numberGuess.getRandomNumber(), shapeGuess.getInitialShapeSeed());
    }
}
