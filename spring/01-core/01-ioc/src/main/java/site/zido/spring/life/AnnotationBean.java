package site.zido.spring.life;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class AnnotationBean {
    private String name;

    @PostConstruct
    public void init() {
        this.name = "init";
    }

    @PreDestroy
    public void destroy() {
        this.name = "destroy";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
