package part1;

import common.BasicTestThread;
import common.PhaseRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class Part1Tester {

    private static final int threadsNum = 256;
    private static final int skierNum = 20000;
    private static final int liftNum = 40;
    private static final String dayId = "23";
    private static final String resortID = "SilverMt";

    public static void main(String[] args) {
        AtomicInteger postSuccessNum = new AtomicInteger();
        AtomicInteger postFailedNum = new AtomicInteger();
        AtomicInteger getSuccessNum = new AtomicInteger();
        AtomicInteger getFailedNum = new AtomicInteger();

        PhaseRunner phase1 = generatePhaseRunner(threadsNum / 4, postSuccessNum, postFailedNum,
                getSuccessNum, getFailedNum, 100, 5, 1, 90);
        PhaseRunner phase2 = generatePhaseRunner(threadsNum, postSuccessNum, postFailedNum,
                getSuccessNum, getFailedNum, 100, 5, 91, 360);
        PhaseRunner phase3 = generatePhaseRunner(threadsNum / 4, postSuccessNum, postFailedNum,
                getSuccessNum, getFailedNum, 100, 10, 361, 420);

        Thread phase1Thread = new Thread(phase1);
        Thread phase2Thread = new Thread(phase2);
        Thread phase3Thread = new Thread(phase3);
        phase1.setNextPhase(phase2Thread);
        phase2.setNextPhase(phase3Thread);

        long start = System.currentTimeMillis();
        phase1Thread.start();
        try {
            phase1Thread.join();
            phase2Thread.join();
            phase3Thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        System.out.printf("Number of successful requests: %d\n",
                postSuccessNum.get() + getSuccessNum.get());
        System.out.printf("number of unsuccessful requests: %d\n",
                postFailedNum.get() + getFailedNum.get());
        System.out.printf("Total wall time: %d\n", end - start);
        System.out.printf("Throughput: %d\n",
                (postFailedNum.get() + postSuccessNum.get() + getFailedNum.get() + getSuccessNum.get()) / ((end - start) / 1000));
    }

    private static PhaseRunner generatePhaseRunner(int numThreads, AtomicInteger postSuccessNum,
                                                   AtomicInteger postFailedNum,
                                                   AtomicInteger getSuccessNum,
                                                   AtomicInteger getFailedNum, int numPosts,
                                                   int numGets, int timeStart, int timeEnd) {
        List<BasicTestThread> phaseThreads = new ArrayList<>();
        int skiersAvg = skierNum / numThreads;
        CountDownLatch phaseRounded = new CountDownLatch(numThreads / 10);
        CountDownLatch phaseTotal = new CountDownLatch(numThreads);
        for (int i = 0; i < numThreads; i++) {
            phaseThreads.add(new Part1TestThread(i * skiersAvg + 1, (i + 1) * skiersAvg,
                    timeStart, timeEnd,
                    liftNum, dayId, resortID, numPosts, numGets, phaseRounded, phaseTotal,
                    postSuccessNum,
                    postFailedNum, getSuccessNum, getFailedNum));
        }

        return new PhaseRunner(phaseThreads, null, phaseRounded, phaseTotal);
    }
}