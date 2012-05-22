package jlm.core.model;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import java.util.*;

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

    public static Map<String, ServerUserData> parse(String answer) {
        Map<String, ServerUserData> data = new HashMap<String, ServerUserData>();

        System.out.println(answer);
        JSONObject dataMap = (JSONObject) JSONValue.parse(answer);
        // for each user
        for (String user : dataMap.keySet()) {
            JSONObject userMap = (JSONObject) dataMap.get(user);
            ServerUserData sud = new ServerUserData();
            sud.setUsername((String) userMap.get("username"));

            String lastJoinString = (String) userMap.get("lastJoin");
            sud.setLastJoin(lastJoinString.equals("null") ? null : new Date(lastJoinString));

            String lastHeartbeatString = (String)userMap.get("lastHeartbeat");
            sud.setLastHeartbeat(lastHeartbeatString.equals("null") ? null : new Date(lastHeartbeatString));

            String lastLeaveString = (String)userMap.get("lastLeave");
            sud.setLastLeave(lastLeaveString.equals("null") ? null : new Date(lastLeaveString));

            JSONArray exercisesArray = (JSONArray) userMap.get("exercises");
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

            data.put(user, sud);
        }

        return data;
    }

    public int getExercisesPassed(){
        int exercisesPassed = 0;
        for(ServerExerciseData exercise: exercises){
            exercisesPassed += exercise.getPassedTests();
        }

        return exercisesPassed;
    }

    public int getExercisesTotal(){
        int exercisesTotal = 0;
        for(ServerExerciseData exercise: exercises){
            exercisesTotal += exercise.getTotalTests();
        }

        return exercisesTotal;
    }
}
