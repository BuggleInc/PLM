package plm.judge;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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

	private ExerciseRunner exerciseRunner = new ExerciseRunner(new Locale("en"));
	private List<BasicListener> listeners = new ArrayList<BasicListener>();
	private Long defaultTimeout = new Long(15000);

	public Judge(Connector connector) {
		this.connector = connector;

		long start = System.currentTimeMillis();
		Logger.info("Warming up compilers and languages");
		warmup("Python");
		warmup("Java");
		warmup("Scala");
		long now = System.currentTimeMillis();
		Logger.info("Everything should be warm now (took "+(now-start)+" ms).");
	}

	public void warmup(String progLang) {
		Exercise exo = JSONUtils.jsonStringToExercise(
		       "{\"id\":\"environment.Environment\",\"name\":\"Environment\",\"defaultSourceFiles\":{"+
				"\"Java\":{\"name\":\"Environment\",\"body\":\"\",\"template\":\"$package   import plm.universe.bugglequest.SimpleBuggle;  public class Environment extends SimpleBuggle {\\t@Override \\tpublic void run() {  $body \\t} } \",\"offset\":1,\"correction\":\"$package \\n\\nimport plm.universe.bugglequest.AbstractBuggle;\\n\\npublic class Environment extends AbstractBuggle {\\n\\t@Override\\n\\tpublic void run() { \\n\\t\\t/* BEGIN TEMPLATE */\\n\\t\\t/* BEGIN SOLUTION */\\n\\t\\tforward();\\n\\t\\t/* END SOLUTION */\\n\\t\\t/* END TEMPLATE */\\n\\t}\\n}\\n\",\"error\":\"Error\"},"+
				"\"Scala\":{\"name\":\"Environment\",\"body\":\"\",\"template\":\"$package \\n\\nimport plm.universe.bugglequest.SimpleBuggle;\\n\\nclass Environment extends SimpleBuggle {\\tprotected override def run() { \\n$body\\n\\t}\\n}\\n\",\"offset\":5,\"correction\":\"$package \\n\\nimport plm.universe.bugglequest.AbstractBuggle;\\n\\nclass Environment extends AbstractBuggle {\\n\\tprotected override def run() { \\n\\t\\t/* BEGIN SOLUTION */\\n\\t\\tforward();\\n\\t\\t/* END SOLUTION */\\n\\t}\\n}\\n\",\"error\":\"Error\"},"+
				"\"Blockly\":{\"name\":\"Environment\",\"body\":\"\",\"template\":\"$body\\n\",\"offset\":1,\"correction\":\"# BEGIN SOLUTION \\nforward()\\n# END SOLUTION\\n\",\"error\":\"Error\"},"+
				"\"Python\":{\"name\":\"Environment\",\"body\":\"\",\"template\":\"$body\\n\",\"offset\":1,\"correction\":\"# BEGIN SOLUTION \\nforward()\\n# END SOLUTION\\n\",\"error\":\"Error\"}},"+
			  "\"helps\":{\"en\":\"no help\"},\"tabName\":\"BlankExercise\","+
			  "\"initialWorlds\":[{\"type\":\"plm.universe.bugglequest.BuggleWorld\",\"@jsonId\":1,\"name\":\"Training Camp\",\"width\":7,\"height\":7,"+
				"\"entities\":[{\"type\":\"plm.universe.bugglequest.SimpleBuggle\",\"name\":\"Noob\",\"x\":3,\"y\":3,\"direction\":0,\"bodyColor\":[0,0,0,255],\"brushColor\":[192,192,192,255],\"world\":1,\"inited\":false,\"brushDown\":false}],"+
				"\"cells\":[[{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":0,\"y\":0},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":0,\"y\":1},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":0,\"y\":2},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":0,\"y\":3},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":0,\"y\":4},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":0,\"y\":5},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":0,\"y\":6}],[{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":1,\"y\":0},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":1,\"y\":1},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":1,\"y\":2},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":1,\"y\":3},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":1,\"y\":4},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":1,\"y\":5},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":1,\"y\":6}],[{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":2,\"y\":0},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":2,\"y\":1},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":2,\"y\":2},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":2,\"y\":3},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":2,\"y\":4},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":2,\"y\":5},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":2,\"y\":6}],[{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":3,\"y\":0},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":3,\"y\":1},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":3,\"y\":2},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":3,\"y\":3},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":3,\"y\":4},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":3,\"y\":5},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":3,\"y\":6}],[{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":4,\"y\":0},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":4,\"y\":1},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":4,\"y\":2},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":4,\"y\":3},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":4,\"y\":4},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":4,\"y\":5},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":4,\"y\":6}],[{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":5,\"y\":0},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":5,\"y\":1},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":5,\"y\":2},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":5,\"y\":3},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":5,\"y\":4},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":5,\"y\":5},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":5,\"y\":6}],[{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":6,\"y\":0},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":6,\"y\":1},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":6,\"y\":2},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":6,\"y\":3},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":6,\"y\":4},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":6,\"y\":5},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":6,\"y\":6}]],\"visibleGrid\":true}],"+
		      "\"answerWorlds\":[{\"type\":\"plm.universe.bugglequest.BuggleWorld\",\"@jsonId\":2,\"name\":\"Training Camp\",\"width\":7,\"height\":7,"+
		        "\"entities\":[{\"type\":\"plm.universe.bugglequest.SimpleBuggle\",\"name\":\"Noob\",\"world\":2,\"inited\":true,\"bodyColor\":[0,0,0,255],\"brushColor\":[192,192,192,255],\"x\":3,\"y\":2,\"direction\":0,\"brushDown\":false}],"+
				"\"cells\":[[{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":0,\"y\":0},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":0,\"y\":1},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":0,\"y\":2},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":0,\"y\":3},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":0,\"y\":4},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":0,\"y\":5},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":0,\"y\":6}],[{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":1,\"y\":0},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":1,\"y\":1},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":1,\"y\":2},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":1,\"y\":3},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":1,\"y\":4},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":1,\"y\":5},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":1,\"y\":6}],[{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":2,\"y\":0},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":2,\"y\":1},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":2,\"y\":2},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":2,\"y\":3},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":2,\"y\":4},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":2,\"y\":5},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":2,\"y\":6}],[{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":3,\"y\":0},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":3,\"y\":1},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":3,\"y\":2},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":3,\"y\":3},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":3,\"y\":4},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":3,\"y\":5},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":3,\"y\":6}],[{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":4,\"y\":0},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":4,\"y\":1},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":4,\"y\":2},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":4,\"y\":3},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":4,\"y\":4},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":4,\"y\":5},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":4,\"y\":6}],[{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":5,\"y\":0},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":5,\"y\":1},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":5,\"y\":2},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":5,\"y\":3},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":5,\"y\":4},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":5,\"y\":5},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":5,\"y\":6}],[{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":6,\"y\":0},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":6,\"y\":1},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":6,\"y\":2},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":6,\"y\":3},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":6,\"y\":4},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":6,\"y\":5},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":6,\"y\":6}]],"+
				"\"visibleGrid\":true}]}\n");

		// The same correction works for all progLangs
		runExo(exo, new Locale("en"), progLang, "forward();");
	}
	
	public ExecutionProgress runExo(Exercise exo, Locale locale, String strProgLang, String code) {
		ProgrammingLanguage progLang = ProgrammingLanguage.getProgrammingLanguage(strProgLang);
		ExecutionProgress result = null;
		exo.setSettings(new UserSettings(locale, progLang));
		
		exerciseRunner.setI18n(locale);
		
		try {
			result = exerciseRunner.run(exo, progLang, code).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public void handleMessage() {
		Channel channel = connector.generateChannel();

		Logger.log(0, "Initializing connection to request message queue...");
		initRequestMQ(channel);

		Logger.log(0, "Waiting for request...");
		String message = connector.retrieveMessage(channel, QUEUE_NAME_REQUEST);

		Logger.log(0, "Parsing the request...");
		Logger.debug(message);
		RequestMsg request = new RequestMsg(message);

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
		
		ExecutionProgress result = runExo(exo, request.getLocalization(), request.getLanguage(), request.getCode());

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
