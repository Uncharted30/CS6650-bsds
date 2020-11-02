package dao;

import dao.interfaces.ILiftRideDao;
import entity.LiftRide;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LiftRideDao implements ILiftRideDao {

    private static final LiftRideDao liftRideDao = new LiftRideDao();

    @Override
    public void createLiftRide(LiftRide newLiftRide) throws SQLException {
        String insertQueryStatement = "INSERT INTO LiftRide (SkierId, ResortId, SeasonId, DayId, Time, LiftId, Vertical) " +
                "VALUES (?,?,?,?,?,?,?)";

        try (Connection conn = DataSource.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(insertQueryStatement);
            preparedStatement.setInt(1, newLiftRide.getSkierID());
            preparedStatement.setString(2, newLiftRide.getResortID());
            preparedStatement.setInt(3, 1);
            preparedStatement.setInt(4, newLiftRide.getDayID());
            preparedStatement.setInt(5, newLiftRide.getTime());
            preparedStatement.setInt(6, newLiftRide.getLiftID());
            preparedStatement.setInt(7, newLiftRide.getLiftID() * 10);

            // execute insert SQL statement
            preparedStatement.executeUpdate();
        }
    }

    public static LiftRideDao getLiftRideDao() {
        return liftRideDao;
    }
}
