package common;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class PhaseRunner implements Runnable {

    private List<BasicTestThread> threads;
    private Thread nextPhase;
    private CountDownLatch roundedCounter;
    private CountDownLatch counter;

    public PhaseRunner(List<BasicTestThread> threads, Thread nextPhase,
                       CountDownLatch roundedCounter, CountDownLatch counter) {
        this.threads = threads;
        this.nextPhase = nextPhase;
        this.roundedCounter = roundedCounter;
        this.counter = counter;
    }

    public void setNextPhase(Thread nextPhase) {
        this.nextPhase = nextPhase;
    }

    @Override
    public void run() {
        for (BasicTestThread thread : threads) {
            new Thread(thread).start();
        }

        try {
            roundedCounter.await();
            if (nextPhase != null) nextPhase.start();
            counter.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
