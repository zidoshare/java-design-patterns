package site.zido.design;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FarmTest {
    @Test
    public void testFarm() {
        AFarm farm = (AFarm) XmlReader.getObject();
        Assertions.assertEquals(AFarm.class, farm.getClass());
        Animal animal = farm.newAnimal();
        Plant plant = farm.newPlant();
        Assertions.assertEquals("牛", animal.show());
        Assertions.assertEquals("蔬菜", plant.show());
    }
}
