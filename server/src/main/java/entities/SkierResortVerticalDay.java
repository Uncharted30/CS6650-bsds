package entities;

import com.fasterxml.jackson.annotation.JsonInclude;

public class SkierResortVerticalDay {
    private String resortID;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long dayID;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long skierID;
    private Integer totalVert;

    public SkierResortVerticalDay(String resortID, Long dayID, Long skierID, Integer totalVert) {
        this.resortID = resortID;
        this.dayID = dayID;
        this.skierID = skierID;
        this.totalVert = totalVert;
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

    public Integer getTotalVert() {
        return totalVert;
    }

    public void setTotalVert(Integer totalVert) {
        this.totalVert = totalVert;
    }
}
