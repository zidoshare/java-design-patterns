package site.zido.design;

import org.junit.jupiter.api.Test;

public class StateTest {
    @Test
    public void test() {
        Context context=new Context();    //创建环境
        context.handle();    //处理请求
        context.handle();
        context.handle();
        context.handle();
    }
}
