package service;

import dao.SkierDao;
import entity.SkierResortVertical;

import java.sql.SQLException;

public class SkierService {

    private static final SkierService skierService = new SkierService();

    private static final SkierDao skierDao = SkierDao.getSkierDao();

    public SkierResortVertical handleGetSkierResortVerticalTotal(String resort,
                                                                 int skierId) throws SQLException {
        return skierDao.getSkierResortTotalVertical(skierId, resort);
    }

    public SkierResortVertical handleGetSkierResortVerticalDay(String resortId, int skierId
            , int dayId) throws SQLException {
        return skierDao.getSkierResortDayVertical(skierId, resortId, dayId);
    }

    public static SkierService getSkierService() {
        return skierService;
    }
}
