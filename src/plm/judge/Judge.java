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
				"\"steps\":[],\"about\":\"no about\",\"parameters\":null,"+
				"\"cells\":[[{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":0,\"y\":0},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":0,\"y\":1},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":0,\"y\":2},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":0,\"y\":3},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":0,\"y\":4},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":0,\"y\":5},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":0,\"y\":6}],[{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":1,\"y\":0},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":1,\"y\":1},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":1,\"y\":2},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":1,\"y\":3},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":1,\"y\":4},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":1,\"y\":5},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":1,\"y\":6}],[{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":2,\"y\":0},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":2,\"y\":1},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":2,\"y\":2},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":2,\"y\":3},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":2,\"y\":4},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":2,\"y\":5},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":2,\"y\":6}],[{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":3,\"y\":0},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":3,\"y\":1},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":3,\"y\":2},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":3,\"y\":3},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":3,\"y\":4},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":3,\"y\":5},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":3,\"y\":6}],[{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":4,\"y\":0},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":4,\"y\":1},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":4,\"y\":2},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":4,\"y\":3},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":4,\"y\":4},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":4,\"y\":5},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":4,\"y\":6}],[{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":5,\"y\":0},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":5,\"y\":1},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":5,\"y\":2},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":5,\"y\":3},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":5,\"y\":4},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":5,\"y\":5},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":5,\"y\":6}],[{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":6,\"y\":0},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":6,\"y\":1},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":6,\"y\":2},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":6,\"y\":3},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":6,\"y\":4},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":6,\"y\":5},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":1,\"x\":6,\"y\":6}]],\"visibleGrid\":true,\"easter\":false}],\"answerWorlds\":[{\"type\":\"plm.universe.bugglequest.BuggleWorld\",\"@jsonId\":2,\"name\":\"Training Camp\",\"width\":7,\"height\":7,\"entities\":[{\"type\":\"plm.universe.bugglequest.SimpleBuggle\",\"name\":\"Noob\",\"world\":2,\"inited\":true,\"bodyColor\":[0,0,0,255],\"brushColor\":[192,192,192,255],\"x\":3,\"y\":2,\"direction\":0,\"brushDown\":false}],\"about\":\"<!-- Please don't translate this file but l10n/missions/plm.pot (see https://github.com/oster/PLM/wiki/Working-on-translations) -->\\n<h1>BuggleWorld</h1>\\nThis world was invented by Lyn Turbak, at Wellesley College. It is full of\\nBuggles, little animals understanding simple orders, and offers numerous\\npossibilities of interaction with the world: taking or dropping objects,\\npaint the ground, hit walls, etc.\\n\\n<h2>Methods understood by buggles</h2>\\n<table border=1>\\n<tr><td colspan=2 align=center><b>Moving</b><br/> (See also the note on exceptions, below)</td></tr>\\n  <tr><td><b>Turn left<br/>Turn right<br/>Turn back<br/>Moving forward<br/>Moving back</b></td>\\n      <td>[!java|c]void [/!]left()<br/>\\n          [!java|c]void [/!]right()<br/>\\n          [!java|c]void [/!]back()<br/>\\n          [!java|c]void [/!]forward()[!c]stepForward()[/!] or [!java|c]void [/!]forward([!java|c]int [/!]steps)<br/>\\n          [!java|c]void [/!]backward()[!c]stepBackward()[/!] or backward([!java|c]int [/!]steps)<br/></td></tr>\\n  <tr><td><b>Get X coordinate<br/>Get Y coordinate<br/>Set X coordinate<br/>Set Y coordinate<br/>Set position</b></td>\\n      <td>[!java|c]int [/!]getX()<br/>\\n          [!java|c]int [/!]getY()<br/>\\n          [!java|c]void [/!]setX([!java|c]int [/!]x)<br/>\\n          [!java|c]void [/!]setY([!java|c]int [/!]y)<br/>\\n          [!java|c]void [/!]setPos([!java|c]int [/!]x, [!java|c]int [/!]y)</td></tr>\\n<tr><td colspan=2>Note that the point (0,0) is on the top left corner, as it is often the case in Computer Science.</td></tr>\\n\\n<tr><td colspan=2 align=center><b>Information on the buggle</b></td></tr>\\n  <tr><td><b>Get the color of the body<br/>Set the color of the body</b></td>\\n      <td>[!java|c]Color [/!]getBodyColor()<br/>\\n          [!java|c]void [/!]setBodyColor([!java|c]Color [/!]c)</td></tr>\\t\\t\\t\\t\\n  <tr><td><b>Look for a wall forward<br/>Look for a wall backward</b></td>\\n      <td>[!c]int [/!]isFacingWall()<br/>\\n          [!c]int [/!]isBackingWall()</td></tr>\\t\\t\\t\\t\\n  <tr><td><b>Get heading<br/>Set heading</b><br/>valid directions are:</td>\\n      <td>[!java|c]Direction [/!]getDirection()<br/>\\n          [!java|c]void [/!]setDirection([!java|c]Direction [/!]dir)<br/>\\n          Direction.NORTH, Direction.EAST, Direction.SOUTH and Direction.WEST</td></tr>\\n  <tr><td>Check whether the buggle is currently <b>selected in the interface</b></td>\\n      <td>[!c]int [/!]isSelected()</td></tr>\\n \\n<tr><td colspan=2 align=center><b>About the brush</b></td></tr>\\n  <tr><td><b>Brush down<br/>Brush up<br/>Get brush position</b></td>\\n      <td>[!java|c]void [/!]brushUp()<br/>\\n          [!java|c]void [/!]brushDown()<br/>\\n          [!c]int [/!]isBrushDown()</td></tr>\\n  <tr><td><b>Change the brush color<br/>Get the color of the brush</b></td>\\n      <td>[!java|c]void [/!]setBrushColor([!java|c]Color [/!]c)<br/>\\n          [!java|c]Color [/!]getBrushColor()</td></tr>\\n\\n<tr><td colspan=2 align=center><b>Interacting with the world</b></td></tr>\\n  <tr><td><b>Get the color of the ground</b></td>\\n      <td>[!java|c]Color [/!]getGroundColor()</td></tr>\\n\\n  <tr><td><b>Look for a baggle on the ground<br/>Look for a baggle in bag<br/>Pickup a baggle<br/>Drop a baggle</b><br/>\\n      (see the note on exceptions)</td>\\n      <td>[!c]int [/!]isOverBaggle()<br/>\\n          [!c]int [/!]isCarryingBaggle()<br/>\\n          pickupBaggle()<br/>\\n          dropBaggle()<br/>\\n          &nbsp;</td></tr>\\n\\n  <tr><td><b>Look for a message<br/>Add a message<br/>Read the message<br/>Erase the message</b></td>\\n      <td>[!c]int [/!]isOverMessage()<br/>\\n          writeMessage([!c]char* [/!]msg)<br/>\\n          [!c]char* [/!]readMessage()<br/>\\n          clearMessage()</td></tr>\\n</table>\\n\\n<h2>Valid colors</h2>\\n<table border=1>\\n<tr><td>Name</td><td>Color</td></tr>\\n<tr><td>Color.black</td>    <td BGCOLOR=\\\"#000000\\\">&nbsp;</td></tr>\\n<tr><td>Color.blue</td>     <td BGCOLOR=\\\"#0000FF\\\">&nbsp;</td></tr>\\n<tr><td>Color.cyan</td>     <td BGCOLOR=\\\"#00FFFF\\\">&nbsp;</td></tr>\\n<tr><td>Color.darkGray</td> <td BGCOLOR=\\\"#404040\\\">&nbsp;</td></tr>\\n<tr><td>Color.gray</td>     <td BGCOLOR=\\\"#808080\\\">&nbsp;</td></tr>\\n<tr><td>Color.green</td>    <td BGCOLOR=\\\"#00FF00\\\">&nbsp;</td></tr>\\n<tr><td>Color.lightGray</td><td BGCOLOR=\\\"#C0C0C0\\\">&nbsp;</td></tr>\\n<tr><td>Color.magenta</td>  <td BGCOLOR=\\\"#FF00FF\\\">&nbsp;</td></tr>\\n<tr><td>Color.orange</td>   <td BGCOLOR=\\\"#FFC800\\\">&nbsp;</td></tr>\\n<tr><td>Color.pink</td>     <td BGCOLOR=\\\"#FFAFAF\\\">&nbsp;</td></tr>\\n<tr><td>Color.red</td>      <td BGCOLOR=\\\"#FF0000\\\">&nbsp;</td></tr>\\n<tr><td>Color.white</td>    <td BGCOLOR=\\\"#FFFFFF\\\">&nbsp;</td></tr>\\n<tr><td>Color.yellow</td>   <td BGCOLOR=\\\"#FFFF00\\\">&nbsp;</td></tr>\\n</table>\\n\\n<h2>Note on exceptions</h2>\\nRegular buggles throw a BuggleWallException exception if you ask them to\\ntraverse a wall.  They throw a NoBaggleUnderBuggleException exception if you\\nask them to pickup a baggle from an empty cell, or a\\nAlreadyHaveBaggleException exception if they already carry a baggle.  Trying\\nto drop a baggle on a cell already containing one throws an\\nAlreadyHaveBaggleException exception. \\nDropping a baggle when you have none throws a DontHaveBaggleException.\\n<p>SimpleBuggles (ie, the one used in first exercises) display an error message\\non problem so that you don't need to know what an exception is.</p>\\n\",\"parameters\":null,\"cells\":[[{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":0,\"y\":0},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":0,\"y\":1},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":0,\"y\":2},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":0,\"y\":3},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":0,\"y\":4},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":0,\"y\":5},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":0,\"y\":6}],[{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":1,\"y\":0},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":1,\"y\":1},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":1,\"y\":2},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":1,\"y\":3},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":1,\"y\":4},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":1,\"y\":5},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":1,\"y\":6}],[{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":2,\"y\":0},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":2,\"y\":1},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":2,\"y\":2},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":2,\"y\":3},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":2,\"y\":4},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":2,\"y\":5},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":2,\"y\":6}],[{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":3,\"y\":0},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":3,\"y\":1},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":3,\"y\":2},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":3,\"y\":3},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":3,\"y\":4},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":3,\"y\":5},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":3,\"y\":6}],[{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":4,\"y\":0},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":4,\"y\":1},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":4,\"y\":2},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":4,\"y\":3},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":4,\"y\":4},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":4,\"y\":5},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":4,\"y\":6}],[{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":5,\"y\":0},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":5,\"y\":1},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":5,\"y\":2},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":5,\"y\":3},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":5,\"y\":4},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":5,\"y\":5},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":5,\"y\":6}],[{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":6,\"y\":0},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":6,\"y\":1},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":6,\"y\":2},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":6,\"y\":3},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":6,\"y\":4},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":6,\"y\":5},{\"type\":\"plm.universe.bugglequest.BuggleWorldCell\",\"world\":2,\"x\":6,\"y\":6}]],"+
				"\"visibleGrid\":true,\"easter\":false}]}\n");

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
