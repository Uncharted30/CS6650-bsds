package dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import dao.interfaces.ILiftRideDao;
import entity.LiftRide;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import utils.QueueChannelFactory;

import java.nio.charset.StandardCharsets;

public class LiftRideDao implements ILiftRideDao {

    private static final LiftRideDao liftRideDao = new LiftRideDao();
    private static GenericObjectPool<Channel> mqConnectionPool = null;
    private static final String QUEUE_NAME = System.getProperty("QUEUE_NAME");
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        GenericObjectPoolConfig<com.rabbitmq.client.Connection> config =
                new GenericObjectPoolConfig<>();
        config.setMaxTotal(128);
        mqConnectionPool = new GenericObjectPool<>(new QueueChannelFactory());
    }

    @Override
    public void createLiftRide(LiftRide newLiftRide) throws Exception {
        Channel channel = mqConnectionPool.borrowObject();
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        channel.basicPublish("", QUEUE_NAME, null,
                mapper.writeValueAsString(newLiftRide).getBytes(StandardCharsets.UTF_8));
        mqConnectionPool.returnObject(channel);

//        String insertQueryStatement = "INSERT INTO LiftRide (SkierId, ResortId, SeasonId, DayId, " +
//                "Time, LiftId, Vertical) " +
//                "VALUES (?,?,?,?,?,?,?)";
//        try (Connection conn = DataSource.getConnection()) {
//            PreparedStatement preparedStatement = conn.prepareStatement(insertQueryStatement);
//            preparedStatement.setInt(1, newLiftRide.getSkierID());
//            preparedStatement.setString(2, newLiftRide.getResortID());
//            preparedStatement.setInt(3, 1);
//            preparedStatement.setInt(4, newLiftRide.getDayID());
//            preparedStatement.setInt(5, newLiftRide.getTime());
//            preparedStatement.setInt(6, newLiftRide.getLiftID());
//            preparedStatement.setInt(7, newLiftRide.getLiftID() * 10);
//
//            // execute insert SQL statement
//            preparedStatement.executeUpdate();
//        }
    }

    public static LiftRideDao getLiftRideDao() {
        return liftRideDao;
    }
}
