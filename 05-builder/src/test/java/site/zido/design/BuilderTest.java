package site.zido.design;

import org.junit.Assert;
import org.junit.Test;

public class BuilderTest {
    @Test
    public void testBuilder() {
        ConcreteBuilder builder = new ConcreteBuilder();
        Director director = new Director(builder);
        Product product = director.construct();

        Assert.assertEquals("part1", product.getPart1());
        Assert.assertEquals("part2", product.getPart2());
        Assert.assertEquals("part3", product.getPart3());
    }
}
