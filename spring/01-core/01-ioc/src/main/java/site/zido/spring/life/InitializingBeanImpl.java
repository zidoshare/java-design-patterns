package site.zido.spring.life;

import org.springframework.beans.factory.InitializingBean;

public class InitializingBeanImpl implements InitializingBean {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.name = "after properties";
    }
}
