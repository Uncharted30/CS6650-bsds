package skiers;

import entities.SkierResortVerticalDay;
import entities.SkierResortVerticalTotal;

public class SkierResortVerticalHandler {

    public SkierResortVerticalTotal handleGetSkierResortVerticalTotal(String resort,
                                                                      String skierId) {
        return new SkierResortVerticalTotal(null, "Mission Ridge", 1000);
    }

    public SkierResortVerticalDay handleGetSkierResortVerticalDay(String resortId, String skierId
            , String dayId) {
        return new SkierResortVerticalDay("Mission Ridge", null, null, 1000);
    }
}
