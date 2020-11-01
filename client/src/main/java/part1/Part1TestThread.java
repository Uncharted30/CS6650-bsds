package part1;

import common.BasicTestThread;
import io.swagger.client.ApiException;
import io.swagger.client.model.LiftRide;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class Part1TestThread extends BasicTestThread {


    public Part1TestThread(int skierIDStart, int skierIDEnd, int timeStart, int timeEnd,
                           int liftNum, String dayID, String resortID, int numPosts,
                           int numGets, CountDownLatch roundedCounter, CountDownLatch counter,
                           AtomicInteger postSuccessNum, AtomicInteger postFailedNum,
                           AtomicInteger getDayVerticalSuccessNum, AtomicInteger getDayVerticalFailedNum,
                           AtomicInteger getTotalVerticalSuccessNum, AtomicInteger getTotalVerticalFailedNum,
                           String baseUrl, boolean getTotalVertical) {
        super(skierIDStart, skierIDEnd, timeStart, timeEnd, liftNum, dayID, resortID,
                numPosts, numGets, roundedCounter, counter, postSuccessNum, postFailedNum,
                getDayVerticalSuccessNum, getDayVerticalFailedNum, getTotalVerticalSuccessNum,
                getTotalVerticalFailedNum, baseUrl, getTotalVertical);
    }

    @Override
    public void run() {
        for (int i = 0; i < numPosts; i++) {
            LiftRide liftRide = getPostBody();
            try {
                sendPostRequest(liftRide);
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }

        for (int j = 0; j < numGets; j++) {
            int skierID = generateRandomSkierID();
            try {
                sendGetDayVerticalRequest(String.valueOf(skierID));
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }

        if (getTotalVertical) {
            for (int j = 0; j < numGets; j++) {
                int skierID = generateRandomSkierID();
                try {
                    sendGetTotalVerticalRequest(String.valueOf(skierID));
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }

        roundedCounter.countDown();
        counter.countDown();
        postSuccessNum.addAndGet(postSuccess);
        postFailedNum.addAndGet(postFailed);
        getDayVerticalSuccessNum.addAndGet(getDayVerticalSuccess);
        getDayVerticalFailedNum.addAndGet(getDayVerticalFailed);
        if (getTotalVertical) {
            getTotalVerticalSuccessNum.addAndGet(getTotalVerticalSuccess);
            getTotalVerticalFailedNum.addAndGet(getTotalVerticalFailed);
        }
    }
}
