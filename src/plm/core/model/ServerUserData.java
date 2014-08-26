package plm.core.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * Class that contains the data sent by the server
 * It describes a student and its results
 */
@Deprecated
public class ServerUserData {

    private String username;
    private List<ServerExerciseData> exercises;
    private Date lastJoin;
    private Date lastLeave;
    private Date lastHeartbeat;

    public ServerUserData() {
        exercises = new ArrayList<ServerExerciseData>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<ServerExerciseData> getExercises() {
        return exercises;
    }

    public Date getLastJoin() {
        return lastJoin;
    }

    public void setLastJoin(Date lastJoin) {
        this.lastJoin = lastJoin;
    }

    public Date getLastLeave() {
        return lastLeave;
    }

    public void setLastLeave(Date lastLeave) {
        this.lastLeave = lastLeave;
    }

    public Date getLastHeartbeat() {
        return lastHeartbeat;
    }

    public void setLastHeartbeat(Date lastHeartbeat) {
        this.lastHeartbeat = lastHeartbeat;
    }

    @Override
    public String toString() {
        return "User [username=" + username + ", exercises=" + exercises
                + ", lastJoin=" + lastJoin + ", lastLeave=" + lastLeave
                + ", lastHeartbeat=" + lastHeartbeat + "]";
    }

    /**
     * Method to transform a json response from the server to ServerUserData objects
     * @param answer response to parse
     * @return list of results by student
     */
    public static Map<String, ServerUserData> parse(String answer) {
        Map<String, ServerUserData> data = new HashMap<String, ServerUserData>();

        JSONObject dataMap = (JSONObject) JSONValue.parse(answer);
        // for each user
        for (Object user : dataMap.keySet()) {
            JSONObject userMap = (JSONObject) dataMap.get(user);
            ServerUserData sud = new ServerUserData();
            sud.setUsername((String) userMap.get("username"));

            String lastJoinString = (String) userMap.get("lastJoin");
            sud.setLastJoin(lastJoinString == null ? null : new Date(lastJoinString));

            String lastHeartbeatString = (String)userMap.get("lastHeartbeat");
            sud.setLastHeartbeat(lastHeartbeatString == null ? null : new Date(lastHeartbeatString));

            String lastLeaveString = (String)userMap.get("lastLeave");
            sud.setLastLeave(lastLeaveString == null ? null : new Date(lastLeaveString));

            JSONArray exercisesArray = (JSONArray) userMap.get("exercises");
            // for each exercise done by the user
            for (Object anExercisesArray : exercisesArray) {
                JSONObject exerciseMap = (JSONObject) anExercisesArray;
                ServerExerciseData sed = new ServerExerciseData();
                sed.setName((String) exerciseMap.get("name"));
                sed.setLang((String) exerciseMap.get("lang"));
                sed.setPassedTests((Integer) exerciseMap.get("passedTests"));
                sed.setTotalTests((Integer) exerciseMap.get("totalTests"));
                sed.setSource((String) exerciseMap.get("source"));
                sed.setDate(new Date((String) exerciseMap.get("date")));

                sud.getExercises().add(sed);
            }

            data.put((String)user, sud);
        }

        return data;
    }

    /**
     * Get the number of exercises passed successfully in all exercises done
     * @return the number..
     */
    public int getExercisesPassed(){
        int exercisesPassed = 0;
        for(ServerExerciseData exercise: exercises){
            exercisesPassed += exercise.getPassedTests();
        }

        return exercisesPassed;
    }

    /**
     * Get the total number of exercises performed (successfully or with fail)
     * @return number
     */
    public int getExercisesTotal(){
        int exercisesTotal = 0;
        for(ServerExerciseData exercise: exercises){
            exercisesTotal += exercise.getTotalTests();
        }

        return exercisesTotal;
    }
}
