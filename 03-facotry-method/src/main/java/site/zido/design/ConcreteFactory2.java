package site.zido.design;

public class ConcreteFactory2 implements AbstractFactory {
    @Override
    public Product newProduct() {
        return new ConcreteProduct2();
    }
}
