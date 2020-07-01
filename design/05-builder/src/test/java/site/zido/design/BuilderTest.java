package site.zido.design;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BuilderTest {
    @Test
    public void testBuilder() {
        ConcreteBuilder builder = new ConcreteBuilder();
        Director director = new Director(builder);
        Product product = director.construct();

        Assertions.assertEquals("part1", product.getPart1());
        Assertions.assertEquals("part2", product.getPart2());
        Assertions.assertEquals("part3", product.getPart3());
    }
}
