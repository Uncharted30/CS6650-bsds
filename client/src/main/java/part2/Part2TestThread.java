package part2;

import common.BasicTestThread;
import io.swagger.client.model.LiftRide;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class Part2TestThread extends BasicTestThread {

    private List<Long> currentThreadPostLatencies;
    private List<Long> allPostLatencies;
    private List<Long> currentThreadGetLatencies;
    private List<Long> allGetLatencies;

    public Part2TestThread(int skierIDStart, int skierIDEnd, int timeStart, int timeEnd,
                           int liftNum, String dayID, String resortID, int numPosts, int numGets,
                           CountDownLatch roundedCounter, CountDownLatch counter,
                           AtomicInteger postSuccessNum, AtomicInteger postFailedNum,
                           AtomicInteger getSuccessNum, AtomicInteger getFailedNum,
                           List<Long> allLatencies) {
        super(skierIDStart, skierIDEnd, timeStart, timeEnd, liftNum, dayID, resortID, numPosts,
                numGets, roundedCounter, counter, postSuccessNum, postFailedNum, getSuccessNum,
                getFailedNum);
        this.allLatencies = allLatencies;
        this.currentThreadLatencies = new ArrayList<>();
    }

    @Override
    public void run() {
        for (int i = 0; i < numPosts; i++) {
            LiftRide liftRide = getPostBody();
            long start = System.currentTimeMillis();
            sendPostRequest(liftRide);
            long end = System.currentTimeMillis();
            currentThreadLatencies.add(end - start);
        }

        for (int j = 0; j < numGets; j++) {
            String skierID = String.valueOf(generateRandomSkierID());
            long start = System.currentTimeMillis();
            sendGetRequest(skierID);
            long end = System.currentTimeMillis();
            currentThreadLatencies.
        }

        roundedCounter.countDown();
        counter.countDown();
        postSuccessNum.addAndGet(postSuccess);
        postFailedNum.addAndGet(postFailed);
        getSuccessNum.addAndGet(getSuccess);
        getFailedNum.addAndGet(getFailed);
    }
}
