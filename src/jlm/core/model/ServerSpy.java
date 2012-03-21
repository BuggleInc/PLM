package jlm.core.model;

import jlm.core.model.json.JSONException;
import jlm.core.model.json.JSONObject;
import jlm.core.model.lesson.ExecutionProgress;
import jlm.core.model.lesson.Exercise;

public abstract class ServerSpy implements ProgressSpyListener {

    protected String username;

    public ServerSpy() {
        username = System.getenv("USER");
        if (username == null)
            username = System.getenv("USERNAME");
        if (username == null)
            username = "John Doe";
    }

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

            sendRequest(jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public abstract void sendRequest(String request);
}
