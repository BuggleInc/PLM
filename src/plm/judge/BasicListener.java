package plm.judge;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.json.simple.JSONObject;

import com.rabbitmq.client.Channel;

import plm.core.model.json.JSONUtils;
import plm.universe.World;

/**
 * The {@link IWorldView} implementation. Linked to the current {@link Game} instance, and is called every time the world moves. 
 * <p>
 * It does two things : first, it creates update chunks via {@link StreamMsg} to describe the world movement.
 * It also aggregates these messages to send them to the given {@link Channel} at least past the given time delay.
 * @author Tanguy
 * @see StreamMsg
 */
public class BasicListener {

	private static final int MAX_SIZE = 10000;

	private World currWorld;
	private String messageQueue;
	private Connector connector;
	private long delay;
	private ScheduledExecutorService ses;

	/**
	 * The {@link BasicListener} constructor.
	 * @param connector the used connector
	 * @param messageQueue the message queue used to publish messages
	 * @param delay The minimal time between two stream messages.
	 */
	public BasicListener(World world, final Connector connector, final String messageQueue, long delay) {
		this.currWorld = world;
		this.delay = delay;
		this.connector = connector;
		this.messageQueue = messageQueue;
		
		ses = Executors.newSingleThreadScheduledExecutor();
		
		Runnable cmd = new Runnable() {
			@Override
			public void run() {
				if(!currWorld.getSteps().isEmpty()) {
					Channel channel = connector.generateChannel();
					connector.initMQ(channel, messageQueue, false, false, true, null);
					sendOperations(channel, currWorld, MAX_SIZE);
					try {
						channel.close();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (TimeoutException e) {
						e.printStackTrace();
					}
				}
			}
		};

		ses.scheduleAtFixedRate(cmd, 0, delay, TimeUnit.MILLISECONDS);
	}

	public void sendOperations(Channel channel, World currWorld, int nbMessages) {
		try {
			send(channel, JSONUtils.operationsToJSON(currWorld, nbMessages));
		} catch (OutOfMemoryError e) {
			// We want to stop the JVM to be able to restart the judge
			e.printStackTrace();
			System.exit(0);
		}
	}

	public void flush(Channel channel) {
		while(!currWorld.getSteps().isEmpty()) {
			sendOperations(channel, currWorld, MAX_SIZE);
	  	}
	}

	@Override
	public BasicListener clone() {
		return new BasicListener(currWorld, connector, messageQueue, delay);
	}
	
	/**
	 * 
	 * @param msg the message to send as MessageStream
	 */
	@SuppressWarnings("unchecked")
	public void streamOut(String msg) {
		JSONObject res = new JSONObject();
		res.put("cmd", "outputStream");
		res.put("msg", msg);
	}
	
	/**
	 * Sends all accumulated messages.
	 */
	public void send(Channel channel, String message) {
		try {
			channel.basicPublish("", messageQueue, null, message.getBytes("UTF-8"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void stopSes() {
		ses.shutdown();
		try {
			ses.awaitTermination(1L, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IllegalMonitorStateException e) {
			// We want to stop the JVM to be able to restart the judge
			e.printStackTrace();
			System.exit(0);
		}
	}
}
