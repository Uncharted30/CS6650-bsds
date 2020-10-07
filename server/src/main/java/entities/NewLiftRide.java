package entities;

public class NewLiftRide {

    private String resortID;
    private Long dayID;
    private Long skierID;
    private Integer time;
    private Integer liftID;

    public NewLiftRide() {
    }

    public NewLiftRide(String resortID, Long dayID, Long skierID, Integer time, Integer liftID) {
        this.resortID = resortID;
        this.dayID = dayID;
        this.skierID = skierID;
        this.time = time;
        this.liftID = liftID;
    }

    public String getResortID() {
        return resortID;
    }

    public void setResortID(String resortID) {
        this.resortID = resortID;
    }

    public Long getDayID() {
        return dayID;
    }

    public void setDayID(Long dayID) {
        this.dayID = dayID;
    }

    public Long getSkierID() {
        return skierID;
    }

    public void setSkierID(Long skierID) {
        this.skierID = skierID;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getLiftID() {
        return liftID;
    }

    public void setLiftID(Integer liftID) {
        this.liftID = liftID;
    }

    @Override
    public String toString() {
        return "NewLiftRide{" +
                "resortID='" + resortID + '\'' +
                ", dayID=" + dayID +
                ", skierID=" + skierID +
                ", time=" + time +
                ", liftID=" + liftID +
                '}';
    }
}
