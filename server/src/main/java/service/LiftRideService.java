package service;

import dao.LiftRideDao;
import entity.LiftRide;

import java.sql.SQLException;

public class LiftRideService {

    private static final LiftRideDao liftRideDao = LiftRideDao.getLiftRideDao();

    public void addNewLiftRide(LiftRide liftRide) throws SQLException {
        liftRideDao.createLiftRide(liftRide);
    }
}
