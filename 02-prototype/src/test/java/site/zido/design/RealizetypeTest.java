package site.zido.design;

import org.junit.Assert;
import org.junit.Test;

public class RealizetypeTest {
    @Test
    public void realizetype()  throws CloneNotSupportedException{
        Realizetype obj1 = new Realizetype();
        Realizetype obj2=(Realizetype)obj1.clone();
        Assert.assertNotEquals("原型和复制后的实体不能相等",obj1,obj2);
        Assert.assertEquals("值应当相等",obj1.getFlag(),obj2.getFlag());
    }
}