package jlm.core.model;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class to manage course data online
 * It has an id and allows to save/retrieve exos/users results by course
 */
public class Course {

    private static URL teacherServer;
    private static URL courseServer;
    private String courseId;
    private HashMap<String, Integer> studentsResults;
    private HashMap<String, Integer> exoResults;
    
    public Course() { 
        this(null);
    }
    
    public Course(String id){
        courseId = id;
        studentsResults = new HashMap<String, Integer>();
        exoResults = new HashMap<String, Integer>();

        try {
            teacherServer = new URL(Game.getProperty("jlm.appengine.url") + "/teacher");
            courseServer = new URL(Game.getProperty("jlm.appengine.url") + "/course");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a new course on the server
     * For example top_quinson
     */
    public void create(){
        try {
            // Construct data
            String data;
            data = URLEncoder.encode("action", "UTF-8") + "=" + URLEncoder.encode("new", "UTF-8");
            data += "&" + URLEncoder.encode("course", "UTF-8") + "=" + URLEncoder.encode(courseId, "UTF-8");

            // Send data
            URLConnection conn = teacherServer.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);

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

    /**
     * Download updated data from server
     * It loads a list of results by student and by exercise
     */
    public void refresh(){

        // get data from JLMServer
        try {
            // Construct data
            String data;
            data = URLEncoder.encode("action", "UTF-8") + "=" + URLEncoder.encode("refresh", "UTF-8");

            // Send data
            URLConnection conn = teacherServer.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            // Get results
            InputStream is = conn.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            studentsResults = (HashMap<String, Integer>)ois.readObject();
            if(studentsResults != null)
                exoResults = (HashMap<String, Integer>)ois.readObject();

            ois.close();
            wr.close();
            is.close();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(studentsResults);
        System.out.println(exoResults);

    }

    /**
     * Delete the course on the server
     * All student and exercises results will be removed
     */
    public void delete(){
        try {
            // Construct data
            String data;
            data = URLEncoder.encode("action", "UTF-8") + "=" + URLEncoder.encode("remove", "UTF-8");
            data += "&" + URLEncoder.encode("course", "UTF-8") + "=" + URLEncoder.encode(courseId, "UTF-8");

            // Send data
            URLConnection conn = teacherServer.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);

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


    /**
     * Get all courses identifiers from the server
     * It allows to display it in form, to select the current course
     * @return list of all courses on the server
     */
    public ArrayList<String> getAllCoursesId() throws IOException {
        ArrayList<String> ids = new ArrayList<String>();

        // get data from JLMServer
        try {
            // Construct data
            String data = URLEncoder.encode("", "UTF-8");

            // Send data
            URLConnection conn = courseServer.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            // Get results
            InputStream is = conn.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            ids = (ArrayList<String>)ois.readObject();

            ois.close();
            wr.close();
            is.close();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return ids;
    }

    /*
    * Getters and setters...
    */

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public HashMap<String, Integer> getStudentsResults() {
        return studentsResults;
    }

    public void setStudentsResults(HashMap<String, Integer> studentsResults) {
        this.studentsResults = studentsResults;
    }

    public HashMap<String, Integer> getExoResults() {
        return exoResults;
    }

    public void setExoResults(HashMap<String, Integer> exoResults) {
        this.exoResults = exoResults;
    }
}
