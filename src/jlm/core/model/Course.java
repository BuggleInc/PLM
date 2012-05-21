package jlm.core.model;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class to manage course data online It has an id and allows to save/retrieve
 * exos/users results by course
 */
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

    public Course(String id, String password) {
		courseId = id;
		this.password = password;
		teacherPassword = "";
		studentsResults = new HashMap<String, Integer>();
		exoResults = new HashMap<String, Integer>();
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

		String response = sendTeacherRequest(jsonObject.toString());
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

		/*
		 * TODO : convert response into a list of students and exos results with
		 * JSONArray and store it in class arguments
		 */

        return sendTeacherRequest(jsonObject.toString());
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

        return sendTeacherRequest(jsonObject.toString());
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
		response = sendCourseRequest(jsonObject.toString());

		if (response != null && !response.isEmpty()) {
			JSONArray arrayResult = (JSONArray) JSONValue.parse(response);
            for (Object anArrayResult : arrayResult) {
                coursesId.add((String) anArrayResult);
            }
		}

		System.out.println(response);
		return coursesId;
	}

	/**
	 * Method to implement to indicate how/where to send data
	 * 
	 * @param request
	 *            request in json to send to the server
	 */
	public abstract String sendTeacherRequest(String request);

	public abstract String sendCourseRequest(String request);

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
