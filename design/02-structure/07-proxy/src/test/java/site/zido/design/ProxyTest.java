package site.zido.design;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProxyTest {
    @Test
    public void testProxy() {
        Proxy proxy = new Proxy();
        String result = proxy.request();
        Assertions.assertEquals("real", result);
    }
}
