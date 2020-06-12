package site.zido.design;

import org.junit.Assert;
import org.junit.Test;

public class ProxyTest {
    @Test
    public void testProxy() {
        Proxy proxy = new Proxy();
        String result = proxy.request();
        Assert.assertEquals("real", result);
    }
}
