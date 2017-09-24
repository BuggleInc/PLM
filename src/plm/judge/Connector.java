package plm.judge;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

import plm.core.log.Logger;

public class Connector {
	private Connection connection;

	public Connector(String host, int port) {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(host);
		factory.setPort(port);
		try {
			connection = factory.newConnection();
		} catch (IOException e) {
			Logger.error("Host unknown. Aborting...");
			System.exit(1);
	    } catch (TimeoutException e) {
	    	Logger.error("Host timed out. Aborting...");
			System.exit(1);
		}
	}

    public Channel generateChannel() {
        Channel channel = null;
        try {
            channel = connection.createChannel();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return channel;
    }

    public void initMQ(Channel channel, String messageQueue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> args) {
        try {
            channel.queueDeclare(messageQueue, durable, exclusive, autoDelete, args);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public void closeConnections() {
		try {
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    public String retrieveMessage(Channel channel, String messageQueue) {
        QueueingConsumer consumer = new QueueingConsumer(channel);
        try {
            channel.basicConsume(messageQueue, true, consumer);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        QueueingConsumer.Delivery delivery = null;
        try {
            delivery = consumer.nextDelivery();
        } catch (ShutdownSignalException | ConsumerCancelledException
                | InterruptedException e2) {
            e2.printStackTrace();
        }

        String message = "";
        try {
            message = new String(delivery.getBody(),"UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        return message;
    }
}
