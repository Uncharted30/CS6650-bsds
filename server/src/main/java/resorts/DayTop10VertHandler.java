package resorts;

import entities.SkierVerticalTotal;

import java.util.ArrayList;
import java.util.List;

public class DayTop10VertHandler {

    public List<SkierVerticalTotal> handleGetDayTop10Vert(String resort, String dayId) {
        List<SkierVerticalTotal> res = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            res.add(new SkierVerticalTotal(i, (10 - i) * 1000));
        }

        return res;
    }
}
