package dao.interfaces;

import entity.LiftRide;

import java.sql.SQLException;

public interface ILiftRideDao {
    void createLiftRide(LiftRide newLiftRide) throws SQLException;
}
