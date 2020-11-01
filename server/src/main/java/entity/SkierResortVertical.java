package entity;

public class SkierResortVertical {

    private String resortId;
    private int totalVert;

    public SkierResortVertical() {
    }

    public SkierResortVertical(String resortId, int totalVert) {
        this.resortId = resortId;
        this.totalVert = totalVert;
    }

    public String getResortId() {
        return resortId;
    }

    public void setResortId(String resortId) {
        this.resortId = resortId;
    }

    public int getTotalVert() {
        return totalVert;
    }

    public void setTotalVert(int totalVert) {
        this.totalVert = totalVert;
    }

    @Override
    public String toString() {
        return "SkierResortVertical{" +
                "resortId='" + resortId + '\'' +
                ", totalVert=" + totalVert +
                '}';
    }
}
