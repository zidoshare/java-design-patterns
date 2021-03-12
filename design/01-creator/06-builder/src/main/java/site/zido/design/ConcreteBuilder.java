package site.zido.design;

public class ConcreteBuilder extends Builder {
    @Override
    public void buildPart1() {
        product.setPart1("part1");
    }

    @Override
    public void buildPart2() {
        product.setPart2("part2");
    }

    @Override
    public void buildPart3() {
        product.setPart3("part3");
    }
}
