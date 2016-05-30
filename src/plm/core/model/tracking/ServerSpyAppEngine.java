package plm.core.model.tracking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import plm.core.model.Game;
import plm.core.model.LogHandler;

/**
 * Implementation of the ServerSpy class, to indicate how/where to send json requests
 * It opens a connection with an App Engine servlet, and send it the request
 * It listens for answer from the server
 */
public class ServerSpyAppEngine extends ServerSpy {

    private URL server;

    public ServerSpyAppEngine(Game game) {
        super(game);

        try {
            server = new URL(Game.getProperty(Game.PROP_APPENGINE_URL) + "/student");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public String sendRequest(String request) {
        String response = "";
        try {

            // Send data
            URLConnection conn = server.openConnection();
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
        	getGame().getLogger().log(LogHandler.ERROR, "Unable to contact PLMServer to send request " + request);
        }

        return response;
    }

	@Override
	public void callForHelp(String studentInput) {
		//TODO
	}

	@Override
	public void cancelCallForHelp() {
		//TODO
	}

	@Override
	public void idle(String start, String end, String duration) {}

	@Override
	public void readTip(String id, String mission) {
		// TODO Auto-generated method stub

	}

}
