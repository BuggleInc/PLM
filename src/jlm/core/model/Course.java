package jlm.core.model;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Course {

    protected String courseId;
    protected String password;
    protected String teacherPassword;
    protected HashMap<String, Integer> studentsResults;
    protected HashMap<String, Integer> exoResults;

    public Course() {
        this(null);
    }

    public Course(String id) {
        courseId = id;
        password = "";
        teacherPassword = "";
        studentsResults = new HashMap<String, Integer>();
        exoResults = new HashMap<String, Integer>();

    }

    public abstract void create();

    public abstract void refresh();

    public abstract void delete();

    public ArrayList<String> getAllCoursesId() {
        String requestResult = getAllCoursesIdRequest();
        ArrayList<String> coursesId = new ArrayList<String>();
        return coursesId;
    }

    public abstract String getAllCoursesIdRequest();

    /*
    * Getters and setters...
    */

    public String getCourseId() {
        if(courseId == null)
            return "";
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
}
