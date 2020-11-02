package dao.interfaces;

import entity.SkierTotalVertical;

import java.sql.SQLException;
import java.util.List;

public interface IResortDao {
    List<SkierTotalVertical> getTop10SkierTotalDayVertical(String resortId, int dayId) throws SQLException;
}
