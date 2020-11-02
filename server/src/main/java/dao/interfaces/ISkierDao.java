package dao.interfaces;

import entity.SkierResortVertical;

import java.sql.SQLException;

public interface ISkierDao {
    SkierResortVertical getSkierResortTotalVertical(int skierId, String resortId) throws SQLException;

    SkierResortVertical getSkierResortDayVertical(int skierId, String resortId, int dayId) throws SQLException;
}
