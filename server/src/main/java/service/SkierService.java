package service;

import dao.SkierDao;
import entity.SkierResortVertical;
import service.interfaces.ISkierService;

import java.sql.SQLException;

public class SkierService implements ISkierService {

    private static final SkierService skierService = new SkierService();

    private static final SkierDao skierDao = SkierDao.getSkierDao();

    @Override
    public SkierResortVertical getSkierResortVerticalTotal(String resort,
                                                                 int skierId) throws SQLException {
        return skierDao.getSkierResortTotalVertical(skierId, resort);
    }

    @Override
    public SkierResortVertical getSkierResortVerticalDay(String resortId, int skierId
            , int dayId) throws SQLException {
        return skierDao.getSkierResortDayVertical(skierId, resortId, dayId);
    }

    public static SkierService getSkierService() {
        return skierService;
    }
}
