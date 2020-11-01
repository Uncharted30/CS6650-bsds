package entity;

public class SkierTotalVertical {
    private long skierID;
    private int verticalTotal;

    public SkierTotalVertical(long skierID, int verticalTotal) {
        this.skierID = skierID;
        this.verticalTotal = verticalTotal;
    }

    public long getSkierID() {
        return skierID;
    }

    public void setSkierID(long skierID) {
        this.skierID = skierID;
    }

    public int getVerticalTotal() {
        return verticalTotal;
    }

    public void setVerticalTotal(int verticalTotal) {
        this.verticalTotal = verticalTotal;
    }

    @Override
    public String toString() {
        return "SkierVerticalTotal{" +
                "skierID=" + skierID +
                ", verticalTotal=" + verticalTotal +
                '}';
    }
}
