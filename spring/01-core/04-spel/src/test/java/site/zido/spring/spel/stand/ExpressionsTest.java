package site.zido.spring.spel.stand;

import org.junit.jupiter.api.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
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

    /**
     * 配置parser
     */
    @Test
    public void testConfiguration() {
        SpelParserConfiguration config = new SpelParserConfiguration(true, true);

        ExpressionParser parser = new SpelExpressionParser(config);

        // - 空引用自动初始化
        // - 自动集合增长
        Expression exp = parser.parseExpression("list[3]");

        Demo demo = new Demo();

        Object o = exp.getValue(demo);

        assertEquals("", o);
    }

    /**
     * 默认情况下， SpEL  表达式只有在求值时才会进行表达式计算，所以表达式可以在运行时进行动态修改。但如果一个表达式被重复调用的次数很多，那么就必须使用
     * SpelCompiler 编译器来保证性能。
     * SpelCompiler 编译器会将表达式编译成字节码，只有在运行时表达式发生变化时，才会被重新编译。
     * SpelCompiler 编译器适用于被调用的频率较高且表达式不经常发生变化的场景。
     * <p>
     * 编译模式
     * 说明
     * <p>
     * <p>
     * OFF
     * 不启用编译模式（默认）。可在 spring.properties 中通过 spring.expression.compiler.mode 属性进行全局设置。
     * <p>
     * <p>
     * MIXED
     * 在混合模式下，随着时间的推移，表达式会从解释模式自动切换到编译模式。即前面使用解释模式，当调用次数达到某个阈值后，改为使用编译模式。
     * <p>
     * <p>
     * IMMEDIATE
     * 启用编译模式。实际内部在调用第一次之后，才会真正使用编译模式。
     *
     */
    @Test
    public void testCompiler() {
        SpelParserConfiguration config = new SpelParserConfiguration(SpelCompilerMode.IMMEDIATE,
                this.getClass().getClassLoader());

        SpelExpressionParser parser = new SpelExpressionParser(config);

        Expression expr = parser.parseExpression("payload");

        MyMessage message = new MyMessage();

        Object payload = expr.getValue(message);
    }
}
