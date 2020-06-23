package site.zido.design;

public class ConcreteStateB extends State {
    @Override
    public void handle(Context context) {
        System.out.println("当前状态是B.");
        context.setState(new ConcreteStateA());
    }
}
