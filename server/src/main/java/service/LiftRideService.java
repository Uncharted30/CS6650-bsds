package service;

import dao.LiftRideDao;
import entity.LiftRide;
import service.interfaces.ILiftRideService;

import java.sql.SQLException;

public class LiftRideService implements ILiftRideService {

    private static final LiftRideDao liftRideDao = LiftRideDao.getLiftRideDao();

    private static final LiftRideService liftRideService = new LiftRideService();

    @Override
    public void addNewLiftRide(LiftRide liftRide) throws SQLException {
        liftRideDao.createLiftRide(liftRide);
    }

    public static LiftRideService getLiftRideService() {
        return liftRideService;
    }
}
