package jlm.core.model;

import net.minidev.json.JSONObject;
import jlm.core.model.lesson.ExecutionProgress;
import jlm.core.model.lesson.Exercise;

/**
 * Abstract class to send users results to a server It constructs the request to
 * send, you have to extend this class to indicate how/where to send the request
 */
public abstract class ServerSpy implements ProgressSpyListener {

	protected String username;

	public ServerSpy() {
		username = System.getenv("USER");
		if (username == null)
			username = System.getenv("USERNAME");
		if (username == null)
			username = "John Doe";
	}

	/**
	 * Receive an exercise progress from progressSpyListener, and construct the
	 * request to send to a server in json
	 * 
	 * @param exo
	 *            progress data
	 */
	@Override
	public void executed(Exercise exo) {
		JSONObject jsonObject = new JSONObject();

		Game game = Game.getInstance();
		ExecutionProgress lastResult = exo.lastResult;
		String exoCode = exo.getPublicSourceFile(lastResult.language, 0)
				.getBody();

		// Retrieve appropriate parameters regarding the current exercise
		jsonObject.put("username", username);
		jsonObject.put("course", game.getCourseID());
        jsonObject.put("password", game.getCoursePassword());
		jsonObject.put("exoname", exo.getName());
		jsonObject.put("exolang", lastResult.language.toString());
        // passedTests and totalTests are initialized at -1 and 0 in case of compilation error...
		jsonObject.put("passedtests", lastResult.passedTests != -1 ? lastResult.passedTests + "" : 0 + "");
		jsonObject.put("totaltests", lastResult.totalTests != 0 ? lastResult.totalTests + "" : 1 + "");
		jsonObject.put("source", exoCode);
		jsonObject.put("action", "execute");

		sendRequest(jsonObject.toString());
	}

	@Override
	public void switched(Exercise exo) {
		JSONObject jsonObject = new JSONObject();

		Game game = Game.getInstance();
		ExecutionProgress lastResult = exo.lastResult;
		// Retrieve appropriate parameters regarding the new exercise
		jsonObject.put("username", username);
		jsonObject.put("course", game.getCourseID());
        jsonObject.put("password", game.getCoursePassword());
		jsonObject.put("exoname", exo.getName());
		jsonObject.put("exolang", lastResult != null ? lastResult.language.toString() : "");
		jsonObject.put("action", "switch");

		sendRequest(jsonObject.toString());
	}

	/**
	 * Send a presence report to the server
	 */
	@Override
	public void heartbeat() {
		JSONObject jsonObject = new JSONObject();
        Game game = Game.getInstance();
		jsonObject.put("username", username);
		jsonObject.put("action", "heartbeat");
        jsonObject.put("course", game.getCourseID());
        jsonObject.put("password", game.getCoursePassword());

		sendRequest(jsonObject.toString());
	}

	@Override
	public String join() {
		JSONObject jsonObject = new JSONObject();
        Game game = Game.getInstance();
		jsonObject.put("username", username);
		jsonObject.put("action", "join");
        jsonObject.put("course", game.getCourseID());
        jsonObject.put("password", game.getCoursePassword());

        return sendRequest(jsonObject.toString());
	}

	@Override
	public void leave() {
		JSONObject jsonObject = new JSONObject();
        Game game = Game.getInstance();
		jsonObject.put("username", username);
		jsonObject.put("action", "leave");
        jsonObject.put("course", game.getCourseID());
        jsonObject.put("password", game.getCoursePassword());

		sendRequest(jsonObject.toString());
	}

	/**
	 * Method to implement to indicate how/where to send data
	 * 
	 * @param request
	 *            request in json to send to the server
	 */
	public abstract String sendRequest(String request);
}
