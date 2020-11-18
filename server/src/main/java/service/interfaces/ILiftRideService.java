package service.interfaces;

import entity.LiftRide;

import java.sql.SQLException;

public interface ILiftRideService {
    void addNewLiftRide(LiftRide liftRide) throws Exception;
}
