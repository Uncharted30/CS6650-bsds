package entities;

import java.util.Objects;

public class SkierVerticalTotal {
    private long skierId;
    private int verticalTotal;

    public SkierVerticalTotal(long skierId, int verticalTotal) {
        this.skierId = skierId;
        this.verticalTotal = verticalTotal;
    }

    public long getSkierId() {
        return skierId;
    }

    public void setSkierId(long skierId) {
        this.skierId = skierId;
    }

    public int getVerticalTotal() {
        return verticalTotal;
    }

    public void setVerticalTotal(int verticalTotal) {
        this.verticalTotal = verticalTotal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SkierVerticalTotal that = (SkierVerticalTotal) o;
        return skierId == that.skierId &&
                verticalTotal == that.verticalTotal;
    }

    @Override
    public int hashCode() {
        return Objects.hash(skierId, verticalTotal);
    }

    @Override
    public String toString() {
        return "SkierVerticalTotal{" +
                "skierId=" + skierId +
                ", verticalTotal=" + verticalTotal +
                '}';
    }
}
