package site.zido.design;

import org.junit.Assert;
import org.junit.Test;

public class FarmTest {
    @Test
    public void testFarm() {
        AFarm farm = (AFarm) XmlReader.getObject();
        Assert.assertEquals(AFarm.class, farm.getClass());
        Animal animal = farm.newAnimal();
        Plant plant = farm.newPlant();
        Assert.assertEquals("牛", animal.show());
        Assert.assertEquals("蔬菜", plant.show());
    }
}
