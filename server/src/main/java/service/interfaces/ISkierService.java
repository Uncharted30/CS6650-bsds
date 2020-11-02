package service.interfaces;

import entity.SkierResortVertical;

import java.sql.SQLException;

public interface ISkierService {
    SkierResortVertical getSkierResortVerticalTotal(String resort, int skierId) throws SQLException;

    SkierResortVertical getSkierResortVerticalDay(String resortId, int skierId, int dayId) throws SQLException;
}
