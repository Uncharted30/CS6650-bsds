package consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import dao.LiftRideDao;
import entity.LiftRide;
import utils.ConsumerProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class ConsumerThread implements Runnable {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final LiftRideDao liftRideDao = LiftRideDao.getLiftRideDao();
    private static final ConnectionFactory factory = new ConnectionFactory();
    private static final String queueName = ConsumerProperties.QUEUE_NAME;
    private static Connection connection;

    static {
        factory.setHost(ConsumerProperties.MQ_HOST);
        factory.setUsername(ConsumerProperties.MQ_USERNAME);
        factory.setPassword(ConsumerProperties.MQ_PASSWORD);
        try {
            connection = factory.newConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            Channel channel = connection.createChannel();
            channel.queueDeclare(queueName, true, false, false, null);
            channel.basicQos(1);
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String body = new String(delivery.getBody(), "UTF-8");
                LiftRide liftRide = mapper.readValue(body, LiftRide.class);
                try {
                    liftRideDao.createLiftRide(liftRide);
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
            // process messages
            channel.basicConsume(queueName, false, deliverCallback, consumerTag -> {});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
