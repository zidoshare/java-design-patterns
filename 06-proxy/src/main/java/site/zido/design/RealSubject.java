package site.zido.design;

public class RealSubject implements Subject {
    @Override
    public String request() {
        return "real";
    }
}
