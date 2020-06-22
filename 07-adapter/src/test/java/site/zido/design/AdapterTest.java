package site.zido.design;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import site.zido.design.obj.ObjectAdapter;
import site.zido.design.type.Adaptee;
import site.zido.design.type.ClassAdapter;

public class AdapterTest {
    @Test
    public void testTypeAdapter() {
        Target target = new ClassAdapter();
        Assertions.assertEquals("adapter", target.request());
    }

    @Test
    public void testObjAdapter(){
        Target target = new ObjectAdapter(new Adaptee());
        Assertions.assertEquals("adapter", target.request());
    }
}
