package jlm.core.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Implementation of HelpServer that sends requests to an App Engine server
 */
public class HelpAppEngine extends HelpServer {

    private static URL helpServer;

    public HelpAppEngine() {
        try {
            helpServer = new URL(Game.getProperty("jlm.appengine.url") + "/student");
        } catch (MalformedURLException e) {
            e.printStackTrace();
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
            System.out.println("Unable to contact JLMServer to send request " + request);
        }
        return response;
    }

}

