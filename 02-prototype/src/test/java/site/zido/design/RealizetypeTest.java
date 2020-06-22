package site.zido.design;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RealizetypeTest {
    @Test
    public void realizetype() throws CloneNotSupportedException {
        Realizetype obj1 = new Realizetype();
        Realizetype obj2 = (Realizetype) obj1.clone();
        Assertions.assertNotEquals(obj1, obj2, "原型和复制后的实体不能相等");
        Assertions.assertEquals(obj1.getFlag(), obj2.getFlag(), "值应当相等");
    }
}