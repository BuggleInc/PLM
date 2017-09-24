package plm.judge;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;

import plm.core.lang.ProgrammingLanguage;
import plm.core.log.Logger;
import plm.core.model.json.JSONUtils;
import plm.core.model.lesson.ExecutionProgress;
import plm.core.model.lesson.ExecutionProgress.outcomeKind;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Exercise.WorldKind;
import plm.core.model.lesson.ExerciseRunner;
import plm.core.model.lesson.UserSettings;
import plm.universe.World;

public class Judge {

	private final String QUEUE_NAME_REQUEST = "worker_in";

	private Connector connector;

	private ExerciseRunner exerciseRunner;
	private List<BasicListener> listeners = new ArrayList<BasicListener>();
	private Long defaultTimeout = new Long(15000);

	public Judge(Connector connector) {
		this.connector = connector;
	}

	public void handleMessage() {
		Channel channel = connector.generateChannel();

		Logger.log(0, "Initializing connection to request message queue...");
		initRequestMQ(channel);

		Logger.log(0, "Waiting for request...");
		String message = connector.retrieveMessage(channel, QUEUE_NAME_REQUEST);

		Logger.log(0, "Parsing the request...");
		RequestMsg request = new RequestMsg(message);
		exerciseRunner = new ExerciseRunner(request.getLocalization());

		Logger.log(0, "Request parsed");
		Logger.log(0, "Initializing connection to reply and client message queues...");

		String replyQueue = request.getReplyQueue();
		initReplyQueue(channel, replyQueue);

		String clientQueue = request.getClientQueue();
		initClientQueue(channel, clientQueue);

		Logger.log(0, "Sending ack...");
		sendAck(channel, replyQueue);

		Logger.log(0, "Processing execution request...");
		Exercise exo = request.getExercise();
		setListeners(request.getClientQueue(), exo);
		
		ProgrammingLanguage progLang = ProgrammingLanguage.getProgrammingLanguage(request.getLanguage());
		String code = request.getCode();

		exo.setSettings(new UserSettings(request.getLocalization(), progLang));

		ExecutionProgress result = null;
		try {
			result = exerciseRunner.run(exo, progLang, code).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		Logger.log(0, "Execution ended");
		Logger.log(0, "Flushing the remaining operations...");
		stopListeners();
		if(result.outcome != outcomeKind.TIMEOUT) {
			flushListeners(channel);
		}

		Logger.log(0, "Operations flushed");
		Logger.log(0, "Sending closing messages...");
		sendUnsubscribe(channel, clientQueue);
		sendResult(channel, replyQueue, result);

		Logger.log(0, "Terminating");
		closeChannel(channel);
	}

	public void initRequestMQ(Channel channel) {
		// Connect to message queue to retrieve a request execution
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("x-message-ttl", defaultTimeout);
		connector.initMQ(channel, QUEUE_NAME_REQUEST, false, false, false, args);
	}

	public void initReplyQueue(Channel channel, String replyQueue) {
		Map<String, Object> args = new HashMap<>();
		args.put("x-message-ttl", defaultTimeout);
		connector.initMQ(channel, replyQueue, false, false, true, args);
		Logger.log(0, "Received request from '" + replyQueue + "'.");
	}

	public void initClientQueue(Channel channel, String clientQueue) {
		connector.initMQ(channel, clientQueue, false, false, true, null);
	}

	public void setListeners(String clientQueue, Exercise exo) {
		for(World world : exo.getWorlds(WorldKind.CURRENT)) {
			BasicListener l = new BasicListener(world, connector, clientQueue, 1000);
			listeners.add(l);
		}
	}

	public void stopListeners() {
		for(BasicListener l : listeners) {
			l.stopSes();
		}
	}

	public void flushListeners(Channel channel) {
		for(BasicListener l : listeners) {
			l.flush(channel);
		}
	}

	public void sendAck(Channel channel, String messageQueue) {
		Map<String, Object> mapArgs = new HashMap<>();
		String message = JSONUtils.createMessage("ack", mapArgs);

		sendMessage(channel, messageQueue, message);
	}

	public void sendUnsubscribe(Channel channel, String messageQueue) {
		Map<String, Object> mapArgs = new HashMap<>();
		String message = JSONUtils.createMessage("unsubscribe", mapArgs);

		sendMessage(channel, messageQueue, message);
	}

	public void sendResult(Channel channel, String messageQueue, ExecutionProgress result) {
		Map<String, Object> mapArgs = new HashMap<>();
		mapArgs.put("result", result);
		String message = JSONUtils.createMessage("executionResult", mapArgs);

		sendMessage(channel, messageQueue, message);
	}

	public void sendMessage(Channel channel, String messageQueue, String message) {
		try {
			channel.basicPublish("", messageQueue, null, message.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void closeChannel(Channel channel) {
		try {
			channel.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}
}
