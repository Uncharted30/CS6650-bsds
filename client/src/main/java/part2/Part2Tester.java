package part2;

import common.*;
import org.apache.commons.cli.ParseException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class Part2Tester {

    private static int threadsNum;
    private static int skierNum;
    private static int liftNum;
    private static String dayId;
    private static String resortID;
    private static String baseUrl;

    public static void main(String[] args) throws ParseException {
        AtomicInteger postSuccessNum = new AtomicInteger();
        AtomicInteger postFailedNum = new AtomicInteger();
        AtomicInteger getSuccessNum = new AtomicInteger();
        AtomicInteger getFailedNum = new AtomicInteger();
        List<LatencyRecord> allPostLatencies = Collections.synchronizedList(new ArrayList<>());
        List<LatencyRecord> allGetLatencies = Collections.synchronizedList(new ArrayList<>());

        Map<String, String> commands = CommandParser.parse(args);
        threadsNum = Integer.parseInt(commands.get(Constants.MAX_NUM_THREADS));
        skierNum = Integer.parseInt(commands.get(Constants.NUM_SKIERS));
        liftNum = Integer.parseInt(commands.get(Constants.NUM_LIFT));
        dayId = commands.get(Constants.DAY_ID);
        resortID = commands.get(Constants.RESORT_ID);
        baseUrl = commands.get(Constants.IP_PORT);

        PhaseRunner phase1 = generatePhaseRunner(threadsNum / 4, postSuccessNum, postFailedNum,
                getSuccessNum, getFailedNum, 100, 5, 1, 90, allPostLatencies, allGetLatencies);
        PhaseRunner phase2 = generatePhaseRunner(threadsNum, postSuccessNum, postFailedNum,
                getSuccessNum, getFailedNum, 100, 5, 91, 360, allPostLatencies, allGetLatencies);
        PhaseRunner phase3 = generatePhaseRunner(threadsNum / 4, postSuccessNum, postFailedNum,
                getSuccessNum, getFailedNum, 100, 10, 361, 420, allPostLatencies, allGetLatencies);

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

        System.out.println("Start processing latency data......");

        allPostLatencies.sort(Comparator.comparingInt(LatencyRecord::getLatency));
        allGetLatencies.sort(Comparator.comparingInt(LatencyRecord::getLatency));

        System.out.printf("Mean response time for POSTs: %d\n", getMeanLatency(allPostLatencies));
        System.out.printf("Mean response time for GETs: %d\n", getMeanLatency(allGetLatencies));

        System.out.printf("Median response time for POSTs: %d\n",
                allPostLatencies.get(allPostLatencies.size() / 2).getLatency());
        System.out.printf("Median response time for GETs: %d\n",
                allGetLatencies.get(allGetLatencies.size() / 2).getLatency());

        System.out.printf("P99 (99th percentile) response time for POSTs: %d\n",
                allPostLatencies.get((int) (allPostLatencies.size() * 0.99)).getLatency());
        System.out.printf("P99 (99th percentile) response time for GETs: %d\n",
                allGetLatencies.get((int) (allGetLatencies.size() * 0.99)).getLatency());

        System.out.printf("Max response time for POSTs: %d\n",
                allPostLatencies.get(allPostLatencies.size() - 1).getLatency());
        System.out.printf("Max response time for GETs: %d \n",
                allGetLatencies.get(allGetLatencies.size() - 1).getLatency());

        try (FileWriter writer = new FileWriter("PostLatencies.csv")) {
            for (LatencyRecord record : allPostLatencies) {
                writer.write(record.toString() + System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileWriter writer = new FileWriter("GetLatencies.csv")) {
            for (LatencyRecord record : allGetLatencies) {
                writer.write(record.toString() + System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static PhaseRunner generatePhaseRunner(int numThreads, AtomicInteger postSuccessNum,
                                                   AtomicInteger postFailedNum,
                                                   AtomicInteger getSuccessNum,
                                                   AtomicInteger getFailedNum, int numPosts,
                                                   int numGets, int timeStart, int timeEnd,
                                                   List<LatencyRecord> allPostLatencies,
                                                   List<LatencyRecord> allGetLatencies) {
        List<BasicTestThread> phaseThreads = new ArrayList<>();
        int skiersAvg = skierNum / numThreads;
        CountDownLatch phaseRounded = new CountDownLatch(numThreads / 10);
        CountDownLatch phaseTotal = new CountDownLatch(numThreads);
        for (int i = 0; i < numThreads; i++) {
            phaseThreads.add(new Part2TestThread(i * skiersAvg + 1, (i + 1) * skiersAvg,
                    timeStart, timeEnd, liftNum, dayId, resortID, numPosts, numGets, phaseRounded
                    , phaseTotal, postSuccessNum, postFailedNum, getSuccessNum, getFailedNum,
                    allPostLatencies, allGetLatencies, baseUrl));
        }

        return new PhaseRunner(phaseThreads, null, phaseRounded, phaseTotal);
    }

    private static int getMeanLatency(List<LatencyRecord> records) {
        int totalLatency = 0;
        for (LatencyRecord record : records) {
            totalLatency += record.getLatency();
        }
        return totalLatency / records.size();
    }
}
