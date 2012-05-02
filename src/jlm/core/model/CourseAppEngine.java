package jlm.core.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Implementation of Course which works with GAE
 */
public class CourseAppEngine extends Course {

    private static URL teacherServer;
    private static URL courseServer;

    public CourseAppEngine() {
        this(null);
    }

    public CourseAppEngine(String id) {
        super(id);
        try {
            teacherServer = new URL(Game.getProperty("jlm.appengine.url") + "/teacher");
            courseServer = new URL(Game.getProperty("jlm.appengine.url") + "/course");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String sendTeacherRequest(String request){
        return sendRequest(request, teacherServer);
    }

    @Override
    public String sendCourseRequest(String request){
       return sendRequest(request, courseServer);
    }

    public String sendRequest(String request, URL server) {
        String response = "";
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
            	response += line;

            wr.close();
            br.close();
        } catch (IOException e) {
            System.out.println("Unable to contact JLMServer to send request " + request);
        }
        return response;
    }
}
