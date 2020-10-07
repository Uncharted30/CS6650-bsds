package entities;

import com.fasterxml.jackson.annotation.JsonInclude;

public class SkierResortVerticalTotal {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long skierID;
    private String resortID;
    private Integer totalVert;

    public SkierResortVerticalTotal(Long skierID, String resortID, Integer totalVert) {
        this.skierID = skierID;
        this.resortID = resortID;
        this.totalVert = totalVert;
    }

    public Long getSkierID() {
        return skierID;
    }

    public void setSkierID(Long skierID) {
        this.skierID = skierID;
    }

    public String getResortID() {
        return resortID;
    }

    public void setResortID(String resortID) {
        this.resortID = resortID;
    }

    public Integer getTotalVert() {
        return totalVert;
    }

    public void setTotalVert(Integer totalVert) {
        this.totalVert = totalVert;
    }
}
