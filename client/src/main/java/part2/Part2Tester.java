package part2;

import common.*;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.BasicConfigurator;

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
        AtomicInteger getDayVerticalSuccessNum = new AtomicInteger();
        AtomicInteger getDayVerticalFailedNum = new AtomicInteger();
        AtomicInteger getTotalVerticalSuccessNum = new AtomicInteger();
        AtomicInteger getTotalVerticalFailedNum = new AtomicInteger();
        List<LatencyRecord> allPostLatencies = Collections.synchronizedList(new ArrayList<>());
        List<LatencyRecord> allGetDayVerticalLatencies =
                Collections.synchronizedList(new ArrayList<>());
        List<LatencyRecord> allGetTotalVerticalLatencies =
                Collections.synchronizedList(new ArrayList<>());

        Map<String, String> commands = CommandParser.parse(args);
        threadsNum = Integer.parseInt(commands.get(Constants.MAX_NUM_THREADS));
        skierNum = Integer.parseInt(commands.get(Constants.NUM_SKIERS));
        liftNum = Integer.parseInt(commands.get(Constants.NUM_LIFT));
        dayId = commands.get(Constants.DAY_ID);
        resortID = commands.get(Constants.RESORT_ID);
        baseUrl = commands.get(Constants.IP_PORT);

        PhaseRunner phase1 = generatePhaseRunner(threadsNum / 4, postSuccessNum, postFailedNum,
                getDayVerticalSuccessNum, getDayVerticalFailedNum, getTotalVerticalSuccessNum,
                getTotalVerticalFailedNum, 1000, 5, 1, 90, allPostLatencies,
                allGetDayVerticalLatencies, allGetTotalVerticalLatencies, false);
        PhaseRunner phase2 = generatePhaseRunner(threadsNum, postSuccessNum, postFailedNum,
                getDayVerticalSuccessNum, getDayVerticalFailedNum, getTotalVerticalSuccessNum,
                getTotalVerticalFailedNum, 1000, 5, 91, 360, allPostLatencies,
                allGetDayVerticalLatencies, allGetTotalVerticalLatencies, false);
        PhaseRunner phase3 = generatePhaseRunner(threadsNum / 4, postSuccessNum, postFailedNum,
                getDayVerticalSuccessNum, getDayVerticalFailedNum, getTotalVerticalSuccessNum,
                getTotalVerticalFailedNum, 1000, 10, 361, 420, allPostLatencies,
                allGetDayVerticalLatencies, allGetTotalVerticalLatencies, true);

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
        int totalRequests =
                postSuccessNum.get() + postFailedNum.get() + getDayVerticalSuccessNum.get() +
                        getDayVerticalFailedNum.get() + getTotalVerticalSuccessNum.get() + getDayVerticalFailedNum.get();
        System.out.printf("Number of successful requests: %d\n",
                postSuccessNum.get() + getDayVerticalSuccessNum.get() + getTotalVerticalSuccessNum.get());
        System.out.printf("number of unsuccessful requests: %d\n",
                postFailedNum.get() + getDayVerticalFailedNum.get() + getTotalVerticalFailedNum.get());
        System.out.printf("Total wall time: %d\n", end - start);
        System.out.printf("Throughput: %d\n", totalRequests / ((end - start) / 1000));

        allPostLatencies.sort(Comparator.comparingInt(LatencyRecord::getLatency));
        allGetDayVerticalLatencies.sort(Comparator.comparingInt(LatencyRecord::getLatency));
        allGetTotalVerticalLatencies.sort(Comparator.comparingInt(LatencyRecord::getLatency));

        System.out.printf("Mean response time for POSTs: %d\n", getMeanLatency(allPostLatencies));
        System.out.printf("Mean response time for GET day verticals: %d\n", getMeanLatency(allGetDayVerticalLatencies));
        System.out.printf("Mean response time for GET total verticals: %d\n", getMeanLatency(allGetTotalVerticalLatencies));

        System.out.printf("Median response time for POSTs: %d\n",
                allPostLatencies.get(allPostLatencies.size() / 2).getLatency());
        System.out.printf("Median response time for GET day verticals: %d\n",
                allGetDayVerticalLatencies.get(allGetDayVerticalLatencies.size() / 2).getLatency());
        System.out.printf("Median response time for GET total verticals: %d\n",
                allGetTotalVerticalLatencies.get(allGetTotalVerticalLatencies.size() / 2).getLatency());

        System.out.printf("P99 (99th percentile) response time for POSTs: %d\n",
                allPostLatencies.get((int) (allPostLatencies.size() * 0.99)).getLatency());
        System.out.printf("P99 (99th percentile) response time for GET day verticals: %d\n",
                allGetDayVerticalLatencies.get((int) (allGetDayVerticalLatencies.size() * 0.99)).getLatency());
        System.out.printf("P99 (99th percentile) response time for GET total verticals: %d\n",
                allGetTotalVerticalLatencies.get((int) (allGetTotalVerticalLatencies.size() * 0.99)).getLatency());

//        System.out.printf("Max response time for POSTs: %d\n",
//                allPostLatencies.get(allPostLatencies.size() - 1).getLatency());
//        System.out.printf("Max response time for GETs: %d \n",
//                allGetLatencies.get(allGetLatencies.size() - 1).getLatency());

//        try (FileWriter writer = new FileWriter("PostLatencies.csv")) {
//            for (LatencyRecord record : allPostLatencies) {
//                writer.write(record.toString() + System.lineSeparator());
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        try (FileWriter writer = new FileWriter("GetLatencies.csv")) {
//            for (LatencyRecord record : allGetDayVerticalLatencies) {
//                writer.write(record.toString() + System.lineSeparator());
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private static PhaseRunner generatePhaseRunner(int numThreads, AtomicInteger postSuccessNum,
                                                   AtomicInteger postFailedNum,
                                                   AtomicInteger getDayVerticalSuccessNum,
                                                   AtomicInteger getDayVerticalFailedNum,
                                                   AtomicInteger getTotalVerticalSuccessNum,
                                                   AtomicInteger getTotalVerticalFailedNum,
                                                   int numPosts, int numGets, int timeStart, int timeEnd,
                                                   List<LatencyRecord> allPostLatencies,
                                                   List<LatencyRecord> allGetDayVerticalLatencies,
                                                   List<LatencyRecord> allGetTotalVerticalLatencies,
                                                   boolean getTotalVertical) {
        List<BasicTestThread> phaseThreads = new ArrayList<>();
        int skiersAvg = skierNum / numThreads;
        CountDownLatch phaseRounded = new CountDownLatch(numThreads / 10);
        CountDownLatch phaseTotal = new CountDownLatch(numThreads);
        for (int i = 0; i < numThreads; i++) {
            phaseThreads.add(new Part2TestThread(i * skiersAvg + 1, (i + 1) * skiersAvg,
                    timeStart, timeEnd, liftNum, dayId, resortID, numPosts, numGets, phaseRounded
                    , phaseTotal, postSuccessNum, postFailedNum, getDayVerticalSuccessNum, getDayVerticalFailedNum,
                    getTotalVerticalSuccessNum, getTotalVerticalFailedNum, allPostLatencies,
                    allGetDayVerticalLatencies, allGetTotalVerticalLatencies, baseUrl, getTotalVertical));
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
