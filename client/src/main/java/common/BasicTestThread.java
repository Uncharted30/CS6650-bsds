package common;

import io.swagger.client.ApiException;
import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.LiftRide;
import io.swagger.client.model.SkierVertical;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class BasicTestThread implements Runnable {

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
    protected AtomicInteger getSuccessNum;
    protected AtomicInteger getFailedNum;

    protected int numPosts;
    protected int numGets;
    protected CountDownLatch roundedCounter;
    protected CountDownLatch counter;

    protected int postSuccess;
    protected int postFailed;
    protected int getSuccess;
    protected int getFailed;

    public BasicTestThread(int skierIDStart, int skierIDEnd, int timeStart, int timeEnd,
                           int liftNum, String dayID, String resortID, int numPosts,
                           int numGets, CountDownLatch roundedCounter, CountDownLatch counter,
                           AtomicInteger postSuccessNum, AtomicInteger postFailedNum,
                           AtomicInteger getSuccessNum, AtomicInteger getFailedNum, String baseUrl) {
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
        this.getSuccessNum = getSuccessNum;
        this.getFailedNum = getFailedNum;

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
            System.err.println(e.getCode());
            postFailed++;
            throw e;
        }
    }

    protected int generateRandomSkierID() {
        int skierIDNum = skierIDEnd - skierIDStart + 1;
        return skierIDStart + rand.nextInt(skierIDNum);
    }

    protected void sendGetRequest(String skierID) throws ApiException {
        try {
            SkierVertical skierDayVertical = skiersApi.getSkierDayVertical(resortID, dayID,
                    String.valueOf(skierID));
            getSuccess++;
        } catch (ApiException e) {
            System.err.println(e.getMessage());
            getFailed++;
            throw e;
        }
    }
}
