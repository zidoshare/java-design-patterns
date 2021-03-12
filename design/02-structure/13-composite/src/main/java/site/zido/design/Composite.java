package site.zido.design;

import java.util.ArrayList;

public class Composite implements Component {
    private ArrayList<Component> children = new ArrayList<>();

    @Override
    public void add(Component c) {
        children.add(c);
    }

    @Override
    public void remove(Component c) {
        children.remove(c);
    }

    @Override
    public Component getChild(int i) {
        return children.get(i);
    }

    @Override
    public void operation() {
        for (Component obj : children) {
            (obj).operation();
        }
    }
}
