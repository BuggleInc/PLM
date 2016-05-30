package plm.core.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.simple.JSONObject;

/**
 * Implementation of HelpServer that sends requests to an App Engine server
 */
public class HelpAppEngine extends HelpServer {

    private static URL helpServer;

    private Game game;

    public HelpAppEngine(Game game) {
    	this.game = game;
    	String url = Game.getProperty(Game.PROP_APPENGINE_URL);
    	if (! url.equals("")) { // no configuration were provided
    		try {
    			helpServer = new URL(Game.getProperty(Game.PROP_APPENGINE_URL) + "/student");
    		} catch (MalformedURLException e) {
    			e.printStackTrace();
    		}
    	} else {
    		game.getLogger().log(LogHandler.INFO, "No course server configured");
    	}
    }

    @Override
    public String sendRequest(String request) {
        String response = "";
        try {

            // Send data
            URLConnection conn = helpServer.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(request);
            wr.flush();

            // Get response data and print it
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = br.readLine()) != null)
            	response += line;

            wr.close();
            br.close();
        } catch (IOException e) {
        	game.getLogger().log(LogHandler.ERROR, "Unable to contact PLMServer to send request " + request);
        }
        return response;
    }

    /**
     * Construct a request to ask teacher help in a course
     */
    @Override
    public void setStatus(boolean isRequestingHelp){
    	super.setStatus(isRequestingHelp);

        JSONObject jsonObject = new JSONObject();
		jsonObject.put("username", username);
		jsonObject.put("action", "help");
        jsonObject.put("course", game.getCourseID());
        jsonObject.put("password", game.getCoursePassword());
        jsonObject.put("status", isRequestingHelp ? "true" : "false");

		sendRequest(jsonObject.toString());
    }

}
