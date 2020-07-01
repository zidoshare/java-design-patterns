package site.zido.demo.spel;

import org.junit.jupiter.api.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.*;

public class ExpressionsTest {

    /**
     * 简单的SpEL字符串
     */
    @Test
    public void testSimpleString() {
        String str = "Hello World";
        ExpressionParser parser = new SpelExpressionParser();
        //'Hello world'
        Expression exp = parser.parseExpression("'Hello world'");
        String msg = (String) exp.getValue();
        assertEquals(str, msg);
    }

    /**
     * 调用字符串中的方法
     */
    @Test
    public void testCallStringMethod() {
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("'Hello World'.concat('!')");
        String msg = (String) exp.getValue();
        assertEquals("Hello World!", msg);
    }

    /**
     * 调用字符串的属性
     */
    @Test
    public void testCallStringProperty() {
        ExpressionParser parser = new SpelExpressionParser();
        //调用getBytes()方法
        Expression exp = parser.parseExpression("'Hello World'.bytes");
        byte[] bytes = (byte[]) exp.getValue();
        assertArrayEquals("Hello World".getBytes(), bytes);
    }

    /**
     * 调用嵌套的属性
     */
    @Test
    public void testNestProperties() {
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("'Hello World'.bytes.length");
        int len = (Integer) exp.getValue();
        assertEquals(11, len);
    }

    /**
     * 调用构造器
     */
    @Test
    public void testInvokeStringConstructor() {
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("new String('hello world').toUpperCase()");
        String msg = exp.getValue(String.class);
        assertEquals("HELLO WORLD", msg);
    }

    /**
     * 设置root
     */
    @Test
    public void testWithRoot() {
        GregorianCalendar c = new GregorianCalendar();
        c.set(1856, Calendar.AUGUST, 9);

        Inventor tesla = new Inventor("Nikola Tesla", c.getTime(), "Serbian");

        SpelExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("name");
        String name = (String) exp.getValue(tesla);

        assertEquals("Nikola Tesla", name);

        exp = parser.parseExpression("name == 'Nikola Tesla'");
        boolean result = exp.getValue(tesla, Boolean.class);
        assertTrue(result);
    }

    // ------------------------------------------------------------------------------------------ //
    // ------------------------------Evaluation-------------------------------------------------- //
    // ------------------------------------------------------------------------------------------ //

    /**
     * 使用setValue()
     */
    @Test
    public void testSetValue() {
        Simple simple = new Simple();
        simple.booleanList.add(true);

        SimpleEvaluationContext context = SimpleEvaluationContext.forReadOnlyDataBinding().build();
        SpelExpressionParser parser = new SpelExpressionParser();
        parser.parseExpression("booleanList[0]").setValue(context, simple, "false");

        Boolean b = simple.booleanList.get(0);
        assertFalse(b);
    }
}
