package site.zido.design;

public abstract class AbstractClass {
    /**
     * 模板方法
     */
    public void templateMethod() {
        abstractMethod1();
        specificMethod();
        abstractMethod2();
    }

    public void specificMethod() //具体方法
    {
        System.out.println("抽象类中的具体方法被调用...");
    }

    public abstract void abstractMethod1(); //抽象方法1

    public abstract void abstractMethod2(); //抽象方法2
}
