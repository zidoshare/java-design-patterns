package site.zido.design.obj;

import site.zido.design.Target;
import site.zido.design.type.Adaptee;

public class ObjectAdapter implements Target {
    private Adaptee adaptee;

    public ObjectAdapter(Adaptee adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public String request() {
        return adaptee.specificRequest();
    }
}
