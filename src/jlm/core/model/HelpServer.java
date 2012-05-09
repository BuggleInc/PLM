package jlm.core.model;

import net.minidev.json.JSONObject;

import javax.swing.*;

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

    public void requestHelp(){

        JSONObject jsonObject = new JSONObject();
		jsonObject.put("username", username);
		jsonObject.put("action", "help");
        jsonObject.put("course", Game.getInstance().getCourseID());
        jsonObject.put("password", Game.getInstance().getCoursePassword());
        jsonObject.put("status", isRequestingHelp ? "on" : "off");

		sendRequest(jsonObject.toString());
    }

    public void switchStatus(JButton helpButton){
        isRequestingHelp = !isRequestingHelp;
        helpButton.setText(isRequestingHelp ? "Cancel Help" : "Help");
    }

   	/**
	 * Method to implement to indicate how/where to send data
	 *
	 * @param request
	 *            request in json to send to the server
	 */
    public abstract String sendRequest(String request);

}
