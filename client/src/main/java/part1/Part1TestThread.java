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
                           AtomicInteger getSuccessNum, AtomicInteger getFailedNum,
                           String baseUrl) {
        super(skierIDStart, skierIDEnd, timeStart, timeEnd, liftNum, dayID, resortID,
                numPosts, numGets, roundedCounter, counter, postSuccessNum, postFailedNum,
                getSuccessNum, getFailedNum, baseUrl);
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
                sendGetRequest(String.valueOf(skierID));
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }

        roundedCounter.countDown();
        counter.countDown();
        postSuccessNum.addAndGet(postSuccess);
        postFailedNum.addAndGet(postFailed);
        getSuccessNum.addAndGet(getSuccess);
        getFailedNum.addAndGet(getFailed);
    }
}
