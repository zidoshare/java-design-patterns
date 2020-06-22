package site.zido.design;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FactoryTest {
    @Test
    public void testNewProduct() {
        AbstractFactory factory = (AbstractFactory) XmlReader.getObject();
        Product product = factory.newProduct();
        Assertions.assertEquals(1, product.show());
    }
}
