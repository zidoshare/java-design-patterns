package site.zido.design;

import org.junit.jupiter.api.Test;

public class ObserverTest {
    @Test
    public void test() {
        Subject subject = new ConcreteSubject();
        Observer obs1 = new ConcreteObserver1();
        Observer obs2 = new ConcreteObserver2();
        subject.add(obs1);
        subject.add(obs2);
        subject.notifyObserver();
    }
}
