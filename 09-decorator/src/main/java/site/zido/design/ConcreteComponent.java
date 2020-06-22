package site.zido.design;

public class ConcreteComponent implements Component {
    public ConcreteComponent() {
        System.out.println("创建具体构件角色");
    }

    @Override
    public int operation() {
        return 1;
    }
}
