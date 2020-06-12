package site.zido.design;

import org.junit.Assert;
import org.junit.Test;

public class FactoryTest {
    @Test
    public void testNewProduct() {
        AbstractFactory factory = (AbstractFactory) XmlReader.getObject();
        Product product = factory.newProduct();
        Assert.assertEquals(1, product.show());
    }
}
