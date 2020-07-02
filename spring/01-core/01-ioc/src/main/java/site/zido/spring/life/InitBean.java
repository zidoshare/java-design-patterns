package site.zido.spring.life;

public class InitBean {
    private String name;

    public void init() {
        this.name = "init";
    }

    public void destroy() {
        this.name = "destroy";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
