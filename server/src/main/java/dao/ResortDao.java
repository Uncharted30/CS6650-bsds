package dao;

import entity.SkierTotalVertical;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResortDao {

    private static final ResortDao resortDao = new ResortDao();

    public List<SkierTotalVertical> getTop10SkierTotalDayVertical(String resortId, int dayId) throws SQLException {
        String queryStatement = "SELECT Resort.ResortId, Skier.SkierId, SUM(Vertical) AS VERTICAL_TOTAL" +
                " FROM Skier LEFT OUTER JOIN LiftRide ON Skier.SkierId = LiftRide.SkierId LEFT " +
                "OUTER JOIN Resort ON Resort.ResortId = LiftRide.ResortId WHERE Resort.ResortId =" +
                " ? AND LiftRide.DayId = ? GROUP BY Skier.SkierId";
        Connection connection = DataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(queryStatement);

        preparedStatement.setString(1, resortId);
        preparedStatement.setInt(2, dayId);

        ResultSet resultSet = preparedStatement.executeQuery();

        List<SkierTotalVertical> res = new ArrayList<>();
        while (resultSet.next()) {
            int skier = resultSet.getInt("SkierId");
            int vertical = resultSet.getInt("VERTICAL_TOTAL");
            res.add(new SkierTotalVertical(skier, vertical));
        }

        connection.close();
        return res;
    }

    public static ResortDao getResortDao() {
        return resortDao;
    }
}
