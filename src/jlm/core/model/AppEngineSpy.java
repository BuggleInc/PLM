package jlm.core.model;

import jlm.core.model.lesson.Exercise;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class AppEngineSpy implements ProgressSpyListener {
    
    private String username;
    private URL server;

    public AppEngineSpy() {
        username = System.getenv("USER");
        if (username == null)
            username = System.getenv("USERNAME");
        if (username == null)
            username = "John Doe";

        try {
            server = new URL(Game.getProperty("jlm.appengine.url") + "/student");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void executed(Exercise exo) {
        String exoName = exo.getName();
        String exoLang = exo.lastResult.language.toString();
        boolean success = exo.isSuccessfullyPassed();


        try {
            // Construct data
            String data;
            data = URLEncoder.encode("exoname", "UTF-8") + "=" + URLEncoder.encode(exoName, "UTF-8");
            data += "&" + URLEncoder.encode("exolang", "UTF-8") + "=" + URLEncoder.encode(exoLang, "UTF-8");
            data += "&" + URLEncoder.encode("exosuccess", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(success), "UTF-8");
            data += "&" + URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");

            // Send data
            URLConnection conn = server.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            // Get response data and print it
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while((line = br.readLine()) != null)
                System.out.println(line);

            wr.close();
            br.close();
          
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
