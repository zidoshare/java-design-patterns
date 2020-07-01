package site.zido.design;

public class Decorator implements Component {

    private Component component;

    public Decorator(Component component) {
        this.component = component;
    }

    @Override
    public int operation() {
        return component.operation();
    }
}
