package site.zido.spring.callbacks;

import org.springframework.context.SmartLifecycle;

public class LifeCycleImpl implements SmartLifecycle {

    private boolean running = false;

    @Override
    public void start() {
        running = true;
    }

    @Override
    public void stop() {
        running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }
}
