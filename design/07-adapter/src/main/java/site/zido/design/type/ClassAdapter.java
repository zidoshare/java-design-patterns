package site.zido.design.type;

import site.zido.design.Target;

public class ClassAdapter extends Adaptee implements Target {
    @Override
    public String request() {
        return specificRequest();
    }
}
