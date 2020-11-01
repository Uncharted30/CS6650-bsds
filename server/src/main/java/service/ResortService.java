package service;

import dao.ResortDao;
import entity.SkierTotalVertical;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResortService {

    private static final ResortService resortService = new ResortService();

    private static final ResortDao resortDao = ResortDao.getResortDao();

    public List<SkierTotalVertical> getTop10SkierTotalDayVertical(String resort, int dayId) throws SQLException {
        return resortDao.getTop10SkierTotalDayVertical(resort, dayId);
    }

    public static ResortService getResortService() {
        return resortService;
    }
}
