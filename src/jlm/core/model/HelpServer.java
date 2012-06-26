package jlm.core.model;

import net.minidev.json.JSONObject;

import javax.swing.*;

/**
 * Abstract class that allows the user to ask help to a teacher by clicking on help button
 * This class only constructs the request with JSON, an implementation must be done to send requests
 * For now it's App Engine which is used
 */
public abstract class HelpServer {

    protected String username;
    // the user is already requesting help
    private boolean isRequestingHelp;

	public HelpServer() {
		username = System.getenv("USER");
		if (username == null)
			username = System.getenv("USERNAME");
		if (username == null)
			username = "John Doe";

        isRequestingHelp = false;
	}

    /**
     * Construct a request to ask teacher help in a course
     */
    public void requestHelp(){

        JSONObject jsonObject = new JSONObject();
		jsonObject.put("username", username);
		jsonObject.put("action", "help");
        jsonObject.put("course", Game.getInstance().getCourseID());
        jsonObject.put("password", Game.getInstance().getCoursePassword());
        jsonObject.put("status", isRequestingHelp ? "true" : "false");

		sendRequest(jsonObject.toString());
    }

    /**
     * Enable or disable the help request
     * @param helpButton button to change
     */
    public void switchStatus(JToggleButton helpButton){
        isRequestingHelp = !isRequestingHelp;
        helpButton.setText(isRequestingHelp ? "Cancel" : "Help");
    }

   	/**
	 * Method to implement to indicate how/where to send data
	 *
	 * @param request
	 *            request in json to send to the server
	 */
    public abstract String sendRequest(String request);

}
