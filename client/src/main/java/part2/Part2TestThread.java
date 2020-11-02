package part2;

import common.BasicTestThread;
import common.LatencyRecord;
import io.swagger.client.ApiException;
import io.swagger.client.model.LiftRide;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class Part2TestThread extends BasicTestThread {

    private List<LatencyRecord> currentThreadPostLatencies;
    private List<LatencyRecord> allPostLatencies;
    private List<LatencyRecord> currentThreadGetDayVerticalLatencies;
    private List<LatencyRecord> allGetDayVerticalLatencies;
    private List<LatencyRecord> currentThreadGetTotalVerticalLatencies;
    private List<LatencyRecord> allGetTotalVerticalLatencies;

    public Part2TestThread(int skierIDStart, int skierIDEnd, int timeStart, int timeEnd,
                           int liftNum, String dayID, String resortID, int numPosts, int numGets,
                           CountDownLatch roundedCounter, CountDownLatch counter,
                           AtomicInteger postSuccessNum, AtomicInteger postFailedNum,
                           AtomicInteger getDayVerticalSuccessNum, AtomicInteger getDayVerticalFailedNum,
                           AtomicInteger getTotalVerticalSuccessNum, AtomicInteger getTotalVerticalFailedNum,
                           List<LatencyRecord> allPostLatencies,
                           List<LatencyRecord> allGetDayVerticalLatencies,
                           List<LatencyRecord> allGetTotalVerticalLatencies, String baseUrl,
                           boolean getTotalVertical) {
        super(skierIDStart, skierIDEnd, timeStart, timeEnd, liftNum, dayID, resortID, numPosts,
                numGets, roundedCounter, counter, postSuccessNum, postFailedNum, getDayVerticalSuccessNum,
                getDayVerticalFailedNum, getTotalVerticalSuccessNum, getTotalVerticalFailedNum, baseUrl,
                getTotalVertical);
        this.allPostLatencies = allPostLatencies;
        this.currentThreadPostLatencies = new ArrayList<>();
        this.allGetDayVerticalLatencies = allGetDayVerticalLatencies;
        this.currentThreadGetDayVerticalLatencies = new ArrayList<>();
        this.allGetTotalVerticalLatencies = allGetTotalVerticalLatencies;
        this.currentThreadGetTotalVerticalLatencies = new ArrayList<>();
    }

    @Override
    public void run() {
        for (int i = 0; i < numPosts; i++) {
            LiftRide liftRide = getPostBody();
            int responseCode;

            long start = System.currentTimeMillis();
            try {
                sendPostRequest(liftRide);
                responseCode = 201;
            } catch (ApiException e) {
                responseCode = e.getCode();
            }
            long end = System.currentTimeMillis();
            currentThreadPostLatencies.add(new LatencyRecord(start, "POST", (int) (end - start),
                    responseCode));
        }

        for (int j = 0; j < numGets; j++) {
            String skierID = String.valueOf(generateRandomSkierID());
            int responseCode;

            long start = System.currentTimeMillis();
            try {
                sendGetDayVerticalRequest(skierID);
                responseCode = 200;
            } catch (ApiException e) {
                responseCode = e.getCode();
            }
            long end = System.currentTimeMillis();
            currentThreadGetDayVerticalLatencies.add(new LatencyRecord(start, "GET", (int) (end - start),
                    responseCode));
        }

        if (getTotalVertical) {
            for (int j = 0; j < numGets; j++) {
                String skierID = String.valueOf(generateRandomSkierID());
                int responseCode;

                long start = System.currentTimeMillis();
                try {
                    sendGetTotalVerticalRequest(skierID);
                    responseCode = 200;
                } catch (ApiException e) {
                    responseCode = e.getCode();
                }
                long end = System.currentTimeMillis();
                currentThreadGetTotalVerticalLatencies.add(new LatencyRecord(start, "GET", (int) (end - start),
                        responseCode));
            }
        }

        roundedCounter.countDown();
        counter.countDown();
        allPostLatencies.addAll(currentThreadPostLatencies);
        allGetDayVerticalLatencies.addAll(currentThreadGetDayVerticalLatencies);
        allGetTotalVerticalLatencies.addAll(currentThreadGetTotalVerticalLatencies);
        postSuccessNum.addAndGet(postSuccess);
        postFailedNum.addAndGet(postFailed);
        getDayVerticalSuccessNum.addAndGet(getDayVerticalSuccess);
        getDayVerticalFailedNum.addAndGet(getDayVerticalFailed);
        getTotalVerticalSuccessNum.addAndGet(getTotalVerticalSuccess);
        getTotalVerticalFailedNum.addAndGet(getTotalVerticalFailed);
    }
}
