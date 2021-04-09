package site.zido.design;

import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

public class FluxTest {

    public void testDemo() {
        Flux.fromIterable(getSomeLongList())
                .mergeWith(Flux.interval(100))
                .doOnNext(serviceA::someObserver)
                .map(d -> d * 2)
                .take(3)
                .onErrorResume(errorHandler::fallback)
                .doAfterTerminate(serviceM::incrementTerminate)
                .subscribe(System.out::println);
    }

    private List<Integer> getSomeLongList() {
        return Arrays.asList(1, 2, 3);
    }
}
