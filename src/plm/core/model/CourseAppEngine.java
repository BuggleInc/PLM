package plm.core.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Implementation of Course which works with GAE
 * It overrides Course methods to send requests constructed by it
 */
public class CourseAppEngine extends Course {

    private static URL teacherServer;
    private static URL courseServer;
    private LogHandler logger;
    
    public CourseAppEngine(LogHandler logger) {
        this("");
        this.logger = logger;
    }

    public CourseAppEngine(String id) {
        super(id);
        try {
            teacherServer = new URL(Game.getProperty(Game.PROP_APPENGINE_URL) + "/teacher");
            courseServer = new URL(Game.getProperty(Game.PROP_APPENGINE_URL) + "/course");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public CourseAppEngine(String id, String password) {
        super(id, password);
        try {
            teacherServer = new URL(Game.getProperty(Game.PROP_APPENGINE_URL) + "/teacher");
            courseServer = new URL(Game.getProperty(Game.PROP_APPENGINE_URL) + "/course");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String sendTeacherRequest(String request) throws IOException{
        return sendRequest(request, teacherServer);
    }

    @Override
    public String sendCourseRequest(String request) throws IOException{
       return sendRequest(request, courseServer);
    }

    public String sendRequest(String request, URL server) throws IOException{
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
        	logger.log(LogHandler.ERROR, "Unable to contact PLMServer to send request " + request);
            throw new IOException(e);
        }
        return response;
    }
}
