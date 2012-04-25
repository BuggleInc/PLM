package jlm.core.model;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import jlm.core.model.lesson.ExecutionProgress;
import jlm.core.model.lesson.Exercise;

/**
 * Abstract class to send users results to a server
 * It constructs the request to send, you have to extend this class to indicate how/where to send the request
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
     * Receive an exercise progress from progressSpyListener, and construct the request to send to
     * a server in json
     * @param exo progress data
     */
    @Override
    public void executed(Exercise exo) {
        JSONObject jsonObject = new JSONObject();

        Game game = Game.getInstance();
        ExecutionProgress lastResult = exo.lastResult;

        try {
            // Retrieve appropriate parameters regarding the current exercise
            jsonObject.put("username", username);
            jsonObject.put("course", game.getCourseID());
            jsonObject.put("exoname", exo.getName());
            jsonObject.put("exolang", lastResult.language.toString());
            jsonObject.put("passedtests", lastResult.passedTests + "");
            jsonObject.put("totaltests", lastResult.totalTests + "");
            jsonObject.put("action", "executed");

            sendRequest(jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void switched(Exercise exo) {
        JSONObject jsonObject = new JSONObject();

        Game game = Game.getInstance();
        ExecutionProgress lastResult = exo.lastResult;

        try {
            // Retrieve appropriate parameters regarding the new exercise
            jsonObject.put("username", username);
            jsonObject.put("course", game.getCourseID());
            jsonObject.put("exoname", exo.getName());
            jsonObject.put("exolang", lastResult.language.toString());
            jsonObject.put("action", "switched");

            sendRequest(jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a presence report to the server
     */
    public void heartbeat(){
        JSONObject jsonObject = new JSONObject();

        try{
            jsonObject.put("username", username);
            jsonObject.put("action", "heartbeat");

            sendRequest(jsonObject.toString());
        } catch(JSONException e){
            e.printStackTrace();
        }
    }

    /**
     * Method to implement to indicate how/where to send data
     * @param request request in json to send to the server
     */
    public abstract void sendRequest(String request);
}
