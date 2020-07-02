package site.zido.spring.life;

import org.springframework.beans.factory.DisposableBean;

public class DisposableBeanImpl implements DisposableBean {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void destroy() throws Exception {
        this.name = "before destroy";
    }
}
