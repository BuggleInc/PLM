package plm.core.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * Class to manage course data online
 * It has an id and allows to save/retrieve users results by course
 * It contains lists of students names sorted by criteria :
 * * students needing help (pressed the help button)
 * * good students (>=90% of good answers)
 * * bad students (<=5% of good answers
 * * layabout students (didn't try any exercises)
 */
public abstract class Course {

    protected String courseId;
    protected String password;
    protected String teacherPassword;
    protected Map<String, ServerUserData> serverData;
    protected ArrayList<String> needingHelpStudents;
    protected ArrayList<String> goodStudents;
    protected ArrayList<String> badStudents;
    protected ArrayList<String> layaboutStudents;

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

        return coursesId;
    }

    /**
     * Refresh all the students filter lists from the server
     */
    public void refreshStudentsLists(){
        refreshStudentsNeedingHelp();
        refreshLayaboutStudents();
        refreshBadStudents();
        refreshGoodStudents();
    }

    /**
     * Generic method that constructs a request to get a list of students, depending on a filter
     * @param filter filter to apply in the request
     * @return the list of students
     */
    public ArrayList<String> refreshStudentsFromFilter(String filter) {
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

        return students;
    }

    public void refreshStudentsNeedingHelp() {
        needingHelpStudents = refreshStudentsFromFilter("needinghelp");
    }

    public void refreshLayaboutStudents() {
        layaboutStudents = refreshStudentsFromFilter("ugly");
    }

    public void refreshBadStudents() {
        badStudents = refreshStudentsFromFilter("bad");
    }

    public void refreshGoodStudents() {
        goodStudents = refreshStudentsFromFilter("good");
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

    public ArrayList<String> getNeedingHelpStudents() {
        return needingHelpStudents;
    }

    public void setNeedingHelpStudents(ArrayList<String> needingHelpStudents) {
        this.needingHelpStudents = needingHelpStudents;
    }

    public ArrayList<String> getGoodStudents() {
        return goodStudents;
    }

    public void setGoodStudents(ArrayList<String> goodStudents) {
        this.goodStudents = goodStudents;
    }

    public ArrayList<String> getBadStudents() {
        return badStudents;
    }

    public void setBadStudents(ArrayList<String> badStudents) {
        this.badStudents = badStudents;
    }

    public ArrayList<String> getLayaboutStudents() {
        return layaboutStudents;
    }

    public void setLayaboutStudents(ArrayList<String> layaboutStudents) {
        this.layaboutStudents = layaboutStudents;
    }
}
