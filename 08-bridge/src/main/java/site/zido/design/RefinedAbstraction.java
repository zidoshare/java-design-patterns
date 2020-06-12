package site.zido.design;

public class RefinedAbstraction extends Abstraction {
    protected RefinedAbstraction(Implementor implementor) {
        super(implementor);
    }

    @Override
    public int operation() {
        return implementor.operation() + 1;
    }
}
