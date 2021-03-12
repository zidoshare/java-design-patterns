package site.zido.design;

public class UnSharedConcreteFlyweight {
    private String info;

    UnSharedConcreteFlyweight(String info){
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
