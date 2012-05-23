package jlm.core.model;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Class to manage course data online It has an id and allows to save/retrieve
 * exos/users results by course
 */
public abstract class Course {

    protected String courseId;
    protected String password;
    protected String teacherPassword;
    protected Map<String, ServerUserData> serverData;

    public Course() {
        this(null);
    }

    public Course(String id) {
        courseId = id;
        password = "";
        teacherPassword = "";
    }

    public Course(String id, String password) {
        courseId = id;
        this.password = password;
        teacherPassword = "";
    }

    /**
     * Create a new course on the server
     * For example "top_quinson"
     * A user password is set to push data, a teacher password to administrate course
     */
    public ServerAnswer create() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action", "new");
        jsonObject.put("course", courseId);
        jsonObject.put("password", password);
        jsonObject.put("teacher_password", teacherPassword);

        String response;

        try {
            response = sendTeacherRequest(jsonObject.toString());
        } catch (IOException e) {
            return null;
        }

        return ServerAnswer.values()[Integer.parseInt(response)];
    }

    /**
     * Download updated data from server It loads a list of results by student
     * and by exercise
     */
    public String refresh() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action", "refresh");
        jsonObject.put("course", courseId);
        jsonObject.put("teacher_password", teacherPassword);

        String answer = null;
        try {
            answer = sendTeacherRequest(jsonObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        // test if the answer was a status code or course data
        try {
            Integer.parseInt(answer);
        } catch (NumberFormatException nfe) {
            serverData = ServerUserData.parse(answer);
        }
        return answer;
    }

    /**
     * Delete the course on the server All student and exercises results will be
     * removed
     */
    public String delete() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action", "remove");
        jsonObject.put("course", courseId);
        jsonObject.put("teacher_password", teacherPassword);

        try {
            return sendTeacherRequest(jsonObject.toString());
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Get all courses identifiers from the server It allows to display it in
     * form, to select the current course
     *
     * @return list of all courses on the server
     */
    public ArrayList<String> getAllCoursesId() {
        String response = "";
        ArrayList<String> coursesId = new ArrayList<String>();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action", "allids");

        try {
            response = sendCourseRequest(jsonObject.toString());
        } catch (IOException e) {
            return null;
        }

        if (response != null && !response.isEmpty()) {
            JSONArray arrayResult = (JSONArray) JSONValue.parse(response);
            for (Object anArrayResult : arrayResult) {
                coursesId.add((String) anArrayResult);
            }
        }

        System.out.println(response);
        return coursesId;
    }

    public ArrayList<String> getStudentsFromFilter(String filter) {
        String answer = "";
        ArrayList<String> students = new ArrayList<String>();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action", filter);
        jsonObject.put("course", courseId);
        jsonObject.put("teacher_password", teacherPassword);

        try {
            answer = sendTeacherRequest(jsonObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        try {
            if (!answer.isEmpty()) {
                JSONArray arrayResult = (JSONArray) JSONValue.parse(answer);
                for (Object result : arrayResult)
                    students.add((String) result);
            }
        } catch (ClassCastException cce) {
            // the answer is a status number, it can't be casted into a list...
            return null;
        }

        System.out.println(answer);
        return students;
    }

    public ArrayList<String> getStudentsNeedingHelp() {
        return getStudentsFromFilter("needinghelp");
    }

    public ArrayList<String> getLayaboutStudents() {
        return getStudentsFromFilter("ugly");
    }

    public ArrayList<String> getBadStudents() {
        return getStudentsFromFilter("bad");
    }

    public ArrayList<String> getGoodStudents() {
        return getStudentsFromFilter("good");
    }

    /**
     * Method to implement to indicate how/where to send data
     *
     * @param request request in json to send to the server
     */
    public abstract String sendTeacherRequest(String request) throws IOException;

    public abstract String sendCourseRequest(String request) throws IOException;

    /*
      * Getters and setters...
      */
    public String getCourseId() {
        if (courseId == null)
            return "";
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTeacherPassword() {
        return teacherPassword;
    }

    public void setTeacherPassword(String teacherPassword) {
        this.teacherPassword = teacherPassword;
    }

    public Map<String, ServerUserData> getServerData() {
        return serverData;
    }

    public void setServerData(Map<String, ServerUserData> serverData) {
        this.serverData = serverData;
    }
}
