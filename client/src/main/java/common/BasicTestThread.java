package common;

import io.swagger.client.ApiException;
import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.LiftRide;
import io.swagger.client.model.SkierVertical;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class BasicTestThread implements Runnable {

    private static final Logger log = Logger.getLogger(BasicTestThread.class.getName());
    private SkiersApi skiersApi;

    private int skierIDStart;
    private int skierIDEnd;
    private int timeStart;
    private int timeEnd;
    private int liftNum;
    private String dayID;
    private String resortID;
    private Random rand;
    protected AtomicInteger postSuccessNum;
    protected AtomicInteger postFailedNum;
    protected AtomicInteger getDayVerticalSuccessNum;
    protected AtomicInteger getDayVerticalFailedNum;
    protected AtomicInteger getTotalVerticalSuccessNum;
    protected AtomicInteger getTotalVerticalFailedNum;

    protected int numPosts;
    protected int numGets;
    protected CountDownLatch roundedCounter;
    protected CountDownLatch counter;

    protected int postSuccess;
    protected int postFailed;
    protected int getDayVerticalSuccess;
    protected int getDayVerticalFailed;
    protected int getTotalVerticalSuccess;
    protected int getTotalVerticalFailed;
    protected boolean getTotalVertical;

    public BasicTestThread(int skierIDStart, int skierIDEnd, int timeStart, int timeEnd,
                           int liftNum, String dayID, String resortID, int numPosts,
                           int numGets, CountDownLatch roundedCounter, CountDownLatch counter,
                           AtomicInteger postSuccessNum, AtomicInteger postFailedNum,
                           AtomicInteger getDayVerticalSuccessNum, AtomicInteger getDayVerticalFailedNum,
                           AtomicInteger getTotalVerticalSuccessNum, AtomicInteger getTotalVerticalFailedNum,
                           String baseUrl, boolean getTotalVertical) {
        this.skierIDStart = skierIDStart;
        this.skierIDEnd = skierIDEnd;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.liftNum = liftNum;
        this.dayID = dayID;
        this.resortID = resortID;
        this.rand = new Random();

        this.numPosts = numPosts;
        this.numGets = numGets;
        this.roundedCounter = roundedCounter;
        this.counter = counter;

        this.postSuccessNum = postSuccessNum;
        this.postFailedNum = postFailedNum;
        this.getDayVerticalSuccessNum = getDayVerticalSuccessNum;
        this.getDayVerticalFailedNum = getDayVerticalFailedNum;
        this.getTotalVerticalSuccessNum = getTotalVerticalSuccessNum;
        this.getTotalVerticalFailedNum = getTotalVerticalFailedNum;

        this.getTotalVertical = getTotalVertical;

        this.skiersApi = new SkiersApi();
        this.skiersApi.getApiClient().setBasePath(baseUrl);
    }

    protected LiftRide getPostBody() {
        int totalTime = timeEnd - timeStart + 1;
        int skierID = generateRandomSkierID();
        int liftID = rand.nextInt(liftNum) + 1;
        int time = timeStart + rand.nextInt(totalTime);
        LiftRide liftRide = new LiftRide();
        liftRide.setResortID(resortID);
        liftRide.setSkierID(String.valueOf(skierID));
        liftRide.setTime(String.valueOf(time));
        liftRide.setDayID(dayID);
        liftRide.setLiftID(String.valueOf(liftID));

        return liftRide;
    }

    protected void sendPostRequest(LiftRide liftRide) throws ApiException {
        try {
            skiersApi.writeNewLiftRide(liftRide);
            postSuccess++;
        } catch (ApiException e) {
            log.debug(liftRide + " " + e.getCode());
            postFailed++;
            throw e;
        }
    }

    protected int generateRandomSkierID() {
        int skierIDNum = skierIDEnd - skierIDStart + 1;
        return skierIDStart + rand.nextInt(skierIDNum);
    }

    protected void sendGetDayVerticalRequest(String skierID) throws ApiException {
        try {
            SkierVertical skierDayVertical = skiersApi.getSkierDayVertical(resortID, dayID,
                    String.valueOf(skierID));
            getDayVerticalSuccess++;
        } catch (ApiException e) {
            log.debug(skierID + " " + e.getCode());
            getDayVerticalFailed++;
            throw e;
        }
    }

    protected void sendGetTotalVerticalRequest(String skierID) throws ApiException {
        try {
            SkierVertical skierTotalVertical = skiersApi.getSkierResortTotals(skierID,
                    Collections.singletonList(resortID));
            getTotalVerticalSuccess++;
        } catch (ApiException e) {
            log.debug(skierID + " " + e.getCode());
            getTotalVerticalFailed++;
            throw e;
        }
    }
}
