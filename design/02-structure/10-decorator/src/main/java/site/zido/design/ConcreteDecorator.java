package site.zido.design;

public class ConcreteDecorator extends Decorator {
    public ConcreteDecorator(Component component) {
        super(component);
    }

    @Override
    public int operation() {
        //TODO 具体装饰行为
        return super.operation();
    }
}
