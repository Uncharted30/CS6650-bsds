package dao;

import entity.SkierResortVertical;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SkierDao {

    private static final SkierDao skierDao = new SkierDao();


    public SkierResortVertical getSkierResortTotalVertical(int skierId, String resortId) throws SQLException {
        String queryStatement = "SELECT Resort.ResortId, SUM(Vertical) AS VERTICAL_TOTAL FROM Skier LEFT OUTER JOIN " +
                "LiftRide ON Skier.SkierId = LiftRide.SkierId LEFT OUTER JOIN Resort ON Resort" +
                ".ResortId = LiftRide.ResortId WHERE Skier.SkierId = ? AND Resort.ResortId = ?";

        try(Connection connection = DataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(queryStatement);
            preparedStatement.setInt(1, skierId);
            preparedStatement.setString(2, resortId);

            ResultSet resultSet = preparedStatement.executeQuery();
            SkierResortVertical skierResortVertical = null;

            if (resultSet.next()) {
                int totalVert = resultSet.getInt("VERTICAL_TOTAL");
                skierResortVertical = new SkierResortVertical(resortId, totalVert);
            }

            return skierResortVertical;
        }
    }

    public SkierResortVertical getSkierResortDayVertical(int skierId, String resortId, int dayId) throws SQLException {
        String queryStatement = "SELECT Resort.ResortId, SUM(Vertical) AS VERTICAL_TOTAL FROM Skier LEFT OUTER JOIN " +
                "LiftRide ON Skier.SkierId = LiftRide.SkierId LEFT OUTER JOIN Resort ON Resort" +
                ".ResortId = LiftRide.ResortId WHERE Skier.SkierId = ? AND Resort.ResortId = ? AND dayId = ?";

        try(Connection connection = DataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(queryStatement);
            preparedStatement.setInt(1, skierId);
            preparedStatement.setString(2, resortId);
            preparedStatement.setInt(3, dayId);

            ResultSet resultSet = preparedStatement.executeQuery();
            SkierResortVertical skierResortVertical = null;

            if (resultSet.next()) {
                int totalVert = resultSet.getInt("VERTICAL_TOTAL");
                skierResortVertical = new SkierResortVertical(resortId, totalVert);
            }

            return skierResortVertical;
        }
    }

    public static SkierDao getSkierDao() {
        return skierDao;
    }
}
