package jlm.core.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class AppEngineSpy extends ServerSpy {

    private URL server;

    public AppEngineSpy() {
        super();

        try {
            server = new URL(Game.getProperty("jlm.appengine.url") + "/student");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void sendRequest(String request) {
        try {

            System.out.println(request);

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
                System.err.println(line);

            wr.close();
            br.close();

        } catch (IOException e) {
            // e.printStackTrace();
            System.out.println("Unable to contact JLMServer");
        }
    }

}
