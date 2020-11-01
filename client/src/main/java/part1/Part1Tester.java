package part1;

import common.BasicTestThread;
import common.CommandParser;
import common.Constants;
import common.PhaseRunner;
import org.apache.commons.cli.ParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class Part1Tester {

    private static int threadsNum;
    private static int skierNum;
    private static int liftNum;
    private static String dayId;
    private static String resortID;
    private static String baseUrl;

    public static void main(String[] args) throws ParseException {
        AtomicInteger postSuccessNum = new AtomicInteger();
        AtomicInteger postFailedNum = new AtomicInteger();
        AtomicInteger getDayVerticalSuccessNum = new AtomicInteger();
        AtomicInteger getDayVerticalFailedNum = new AtomicInteger();
        AtomicInteger getTotalVerticalSuccessNum = new AtomicInteger();
        AtomicInteger getTotalVerticalFailedNum = new AtomicInteger();

        Map<String, String> commands = CommandParser.parse(args);
        threadsNum = Integer.parseInt(commands.get(Constants.MAX_NUM_THREADS));
        skierNum = Integer.parseInt(commands.get(Constants.NUM_SKIERS));
        liftNum = Integer.parseInt(commands.get(Constants.NUM_LIFT));
        dayId = commands.get(Constants.DAY_ID);
        resortID = commands.get(Constants.RESORT_ID);
        baseUrl = commands.get(Constants.IP_PORT);

        PhaseRunner phase1 = generatePhaseRunner(threadsNum / 4, postSuccessNum, postFailedNum,
                getDayVerticalSuccessNum, getDayVerticalFailedNum, getTotalVerticalSuccessNum,
                getTotalVerticalFailedNum, 1000, 5, 1, 90, false);
        PhaseRunner phase2 = generatePhaseRunner(threadsNum, postSuccessNum, postFailedNum,
                getDayVerticalSuccessNum, getDayVerticalFailedNum, getTotalVerticalSuccessNum,
                getTotalVerticalFailedNum, 1000, 5, 91, 360, false);
        PhaseRunner phase3 = generatePhaseRunner(threadsNum / 4, postSuccessNum, postFailedNum,
                getDayVerticalSuccessNum, getDayVerticalFailedNum, getTotalVerticalSuccessNum,
                getTotalVerticalFailedNum, 1000, 10, 361, 420, true);

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
                postSuccessNum.get() + getDayVerticalSuccessNum.get() + getTotalVerticalSuccessNum.get());
        System.out.printf("number of unsuccessful requests: %d\n",
                postFailedNum.get() + getDayVerticalFailedNum.get() + getTotalVerticalFailedNum.get());
        System.out.printf("Total wall time: %d\n", end - start);
        System.out.printf("Throughput: %d\n",
                (postFailedNum.get() + postSuccessNum.get() + getDayVerticalFailedNum.get()
                        + getDayVerticalSuccessNum.get()) + getTotalVerticalSuccessNum.get()
                        + getTotalVerticalFailedNum.get() / ((end - start) / 1000));
    }

    private static PhaseRunner generatePhaseRunner(int numThreads, AtomicInteger postSuccessNum,
                                                   AtomicInteger postFailedNum,
                                                   AtomicInteger getDayVerticalSuccessNum,
                                                   AtomicInteger getDayVerticalFailedNum,
                                                   AtomicInteger getTotalVerticalSuccessNum,
                                                   AtomicInteger getTotalVerticalFailedNum,
                                                   int numPosts,
                                                   int numGets, int timeStart, int timeEnd,
                                                   boolean getTotalVertical) {
        List<BasicTestThread> phaseThreads = new ArrayList<>();
        int skiersAvg = skierNum / numThreads;
        CountDownLatch phaseRounded = new CountDownLatch(numThreads / 10);
        CountDownLatch phaseTotal = new CountDownLatch(numThreads);
        for (int i = 0; i < numThreads; i++) {
            phaseThreads.add(new Part1TestThread(i * skiersAvg + 1, (i + 1) * skiersAvg,
                    timeStart, timeEnd, liftNum, dayId, resortID, numPosts, numGets, phaseRounded
                    , phaseTotal, postSuccessNum, postFailedNum, getDayVerticalSuccessNum,
                    getDayVerticalFailedNum,
                    getTotalVerticalSuccessNum, getTotalVerticalFailedNum, baseUrl,
                    getTotalVertical));
        }

        return new PhaseRunner(phaseThreads, null, phaseRounded, phaseTotal);
    }
}
