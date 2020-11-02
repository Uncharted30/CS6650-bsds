package service.interfaces;

import entity.SkierTotalVertical;

import java.sql.SQLException;
import java.util.List;

public interface IResortService {
    List<SkierTotalVertical> getTop10SkierTotalDayVertical(String resort, int dayId) throws SQLException;
}
