package service;

import dao.ResortDao;
import entity.SkierTotalVertical;
import service.interfaces.IResortService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResortService implements IResortService {

    private static final ResortService resortService = new ResortService();

    private static final ResortDao resortDao = ResortDao.getResortDao();

    @Override
    public List<SkierTotalVertical> getTop10SkierTotalDayVertical(String resort, int dayId) throws SQLException {
        return resortDao.getTop10SkierTotalDayVertical(resort, dayId);
    }

    public static ResortService getResortService() {
        return resortService;
    }
}
