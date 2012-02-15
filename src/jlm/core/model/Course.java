package jlm.core.model;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class Course {

    private static URL teacherServer;
    private static URL courseServer;
    private String courseId;
    private HashMap<String, Integer> usersResults;
    private HashMap<String, Integer> exoResults;
    
    public Course() { 
        this(null);
    }
    
    public Course(String id){
        courseId = id;
        usersResults = new HashMap<String, Integer>();
        exoResults = new HashMap<String, Integer>();

        try {
            teacherServer = new URL(Game.getProperty("jlm.appengine.url") + "/teacher");
            courseServer = new URL(Game.getProperty("jlm.appengine.url") + "/course");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void create(){
        
    }
    
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
            usersResults = (HashMap<String, Integer>)ois.readObject();
            if(usersResults != null)
                exoResults = (HashMap<String, Integer>)ois.readObject();

            ois.close();
            wr.close();
            is.close();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Unable to contact JLMServer");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(usersResults);
        System.out.println(exoResults);

    }
    
    public void delete(){
        
    }

    public static ArrayList<String> getAllCoursesId(){
        ArrayList<String> ids = new ArrayList<String>();

        // get data from JLMServer
        try {
            // Construct data
            String data;
            data = URLEncoder.encode("", "UTF-8");

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
        } catch (IOException e) {
            System.out.println("Unable to contact JLMServer");
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

    public HashMap<String, Integer> getUsersResults() {
        return usersResults;
    }

    public void setUsersResults(HashMap<String, Integer> usersResults) {
        this.usersResults = usersResults;
    }

    public HashMap<String, Integer> getExoResults() {
        return exoResults;
    }

    public void setExoResults(HashMap<String, Integer> exoResults) {
        this.exoResults = exoResults;
    }
}
