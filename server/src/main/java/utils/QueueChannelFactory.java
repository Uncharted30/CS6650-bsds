package utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.DestroyMode;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class QueueChannelFactory extends BasePooledObjectFactory<Channel> {

    private static final String MQ_HOST = System.getProperty("MQ_HOST");
    private static final String USERNAME = System.getProperty("MQ_USERNAME");
    private static final String PASSWORD = System.getProperty("MQ_PASSWORD");
    private static final ConnectionFactory factory = new ConnectionFactory();

    private static Connection connection;

    static {
        factory.setHost(MQ_HOST);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);
        try {
            connection = factory.newConnection();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Channel create() throws Exception {
        return connection.createChannel();
    }

    @Override
    public PooledObject<Channel> wrap(Channel channel) {
        return new DefaultPooledObject<>(channel);
    }

    @Override
    public void destroyObject(PooledObject<Channel> p, DestroyMode mode) throws Exception {
        p.getObject().close();
    }

    @Override
    public boolean validateObject(PooledObject<Channel> p) {
        return p.getObject().isOpen();
    }
}
